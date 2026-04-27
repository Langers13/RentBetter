package com.example.rentbetter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.NavEntry
import com.example.rentbetter.data.SecureStorage
import com.example.rentbetter.ui.screens.WebViewScreen
import com.example.rentbetter.ui.theme.RentBetterTheme
import com.example.rentbetter.viewmodel.WebViewViewModel
import com.example.rentbetter.viewmodel.WebViewViewModelFactory
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val secureStorage = remember { SecureStorage(context) }
            val viewModel: WebViewViewModel = viewModel(
                factory = WebViewViewModelFactory(secureStorage)
            )

            RentBetterTheme {
                val backStack = remember { mutableStateListOf<Screen>(Screen.Home) }
                @Suppress("UNCHECKED_CAST")
                val myEntryProvider = entryProvider {
                    entry<Screen.Home> {
                        MainContent(viewModel, secureStorage)
                    }
                } as (NavKey) -> NavEntry<NavKey>

                NavDisplay(
                    backStack = backStack,
                    entryProvider = myEntryProvider,
                    onBack = {
                        if (backStack.size > 1) {
                            backStack.removeAt(backStack.size - 1)
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(viewModel: WebViewViewModel, secureStorage: SecureStorage) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
    ) { innerPadding ->
        WebViewScreen(
            viewModel = viewModel,
            secureStorage = secureStorage,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Serializable
sealed interface Screen : NavKey {
    @Serializable
    data object Home : Screen
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RentBetterTheme {
        Text("RentBetter Preview")
    }
}
