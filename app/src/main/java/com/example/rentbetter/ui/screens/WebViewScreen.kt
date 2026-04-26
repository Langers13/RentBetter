package com.example.rentbetter.ui.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.rentbetter.viewmodel.WebViewViewModel
import com.example.rentbetter.data.SecureStorage
import androidx.compose.foundation.layout.statusBarsPadding
import android.webkit.WebSettings
import android.webkit.WebChromeClient

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(
    viewModel: WebViewViewModel,
    secureStorage: SecureStorage,
    modifier: Modifier = Modifier
) {
    var isLoading by remember { mutableStateOf(true) }
    var progress by remember { mutableFloatStateOf(0f) }
    var webView: WebView? by remember { mutableStateOf(null) }

    LaunchedEffect(viewModel.refreshTrigger) {
        if (viewModel.refreshTrigger > 0) {
            webView?.reload()
        }
    }

    Column(modifier = modifier.fillMaxSize().statusBarsPadding()) {
        if (isLoading) {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp),
                color = MaterialTheme.colorScheme.tertiary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }

        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    webView = this
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.databaseEnabled = true
                    settings.loadWithOverviewMode = true
                    settings.useWideViewPort = true
                    settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    settings.javaScriptCanOpenWindowsAutomatically = true
                    settings.setSupportMultipleWindows(true)
                    settings.userAgentString = "Mozilla/5.0 (Linux; Android 13; Pixel 7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Mobile Safari/537.36"
                    
                    // Add JS Interface for menu communication
                    addJavascriptInterface(object {
                        @JavascriptInterface
                        fun onMenuToggle(isVisible: Boolean) {
                            viewModel.setMenuVisibility(isVisible)
                        }
                    }, "AndroidBridge")

                    webChromeClient = object : WebChromeClient() {
                        override fun onProgressChanged(view: WebView?, newProgress: Int) {
                            progress = newProgress / 100f
                            if (newProgress == 100) isLoading = false
                        }
                    }

                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            isLoading = true
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            val email = secureStorage.getEmail()
                            val password = secureStorage.getPassword()
                            
                            // 1. Automated Login Injection
                            if (url?.contains("auth.rentbetter.com.au") == true && email != null && password != null) {
                                val loginJs = """
                                    (function() {
                                        var userField = document.querySelector('input[name="username"]') || document.querySelector('input[type="email"]');
                                        var passField = document.querySelector('input[name="password"]');
                                        var btn = document.querySelector('button[type="submit"]');
                                        
                                        if (userField && passField) {
                                            userField.value = '$email';
                                            passField.value = '$password';
                                            setTimeout(function() { if (btn) btn.click(); }, 500);
                                        }
                                    })();
                                """.trimIndent()
                                view?.evaluateJavascript(loginJs, null)
                            }

                            // 2. Hamburger Menu Bridge Injection (Targeting Headless UI & Common Selectors)
                            val menuJs = """
                                (function() {
                                    function checkMenu() {
                                        var headlessBtn = document.querySelector('button[id^="headlessui-disclosure-button-"]');
                                        var menuBtn = headlessBtn ||
                                                     document.querySelector('.w-nav-button') || 
                                                     document.querySelector('.hamburger') || 
                                                     document.querySelector('[aria-label="Menu"]') ||
                                                     document.querySelector('.navbar-toggler') ||
                                                     document.querySelector('.nav-toggle') ||
                                                     document.querySelector('.bubble-element.Button');
                                        
                                        if (menuBtn) {
                                            var isVisible = (menuBtn.getAttribute('aria-expanded') === 'true') ||
                                                            document.body.classList.contains('menu-open') || 
                                                            document.body.classList.contains('nav-open') ||
                                                            document.body.classList.contains('sidebar-open') ||
                                                            menuBtn.classList.contains('is-active') ||
                                                            menuBtn.classList.contains('w--open');
                                            
                                            AndroidBridge.onMenuToggle(isVisible);
                                            
                                            if (isVisible) {
                                                var headlessPanel = document.querySelector('[id^="headlessui-disclosure-panel-"]');
                                                if (headlessPanel) {
                                                    headlessPanel.style.setProperty('display', 'block', 'important');
                                                    headlessPanel.style.setProperty('visibility', 'visible', 'important');
                                                    headlessPanel.style.setProperty('opacity', '1', 'important');
                                                    headlessPanel.style.setProperty('height', 'auto', 'important');
                                                    headlessPanel.style.setProperty('max-height', 'none', 'important');
                                                }

                                                var menuContainers = document.querySelectorAll('.w-nav-menu, .nav-menu, .navbar-collapse, .sidebar, .mobile-nav, .aside-menu, .mobile-sidebar');
                                                menuContainers.forEach(function(c) {
                                                    c.style.setProperty('display', 'block', 'important');
                                                    c.style.setProperty('visibility', 'visible', 'important');
                                                    c.style.setProperty('opacity', '1', 'important');
                                                });
                                            }
                                        }
                                    }

                                    document.addEventListener('click', function(e) {
                                        setTimeout(checkMenu, 150);
                                    }, true);

                                    checkMenu();

                                    var observer = new MutationObserver(function(mutations) {
                                        checkMenu();
                                    });

                                    observer.observe(document.body, { attributes: true, attributeFilter: ['class'] });
                                    
                                    var btn = document.querySelector('button[id^="headlessui-disclosure-button-"]');
                                    if (btn) {
                                        observer.observe(btn, { attributes: true, attributeFilter: ['aria-expanded'] });
                                    }
                                })();
                            """.trimIndent()
                            view?.evaluateJavascript(menuJs, null)
                        }

                        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                            return false
                        }
                    }
                    
                    loadUrl("https://auth.rentbetter.com.au/u/login")
                }
            },
            modifier = Modifier.weight(1f)
        )
    }
}
