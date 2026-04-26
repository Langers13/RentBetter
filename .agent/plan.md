# Project Plan

App Name: RentBetter. 
An app that accesses https://auth.rentbetter.com.au/u/login and automatically logs in with the login credentials stored securely by the app after the first use. 
The app's sole purpose is to view the website. 
A ViewModel is required to handle a tricky hamburger menu on the website within the app view.

## Project Brief

# RentBetter Project Brief

## Features
- **Automated Secure Login**: Securely stores user credentials after the first manual login and automatically authenticates subsequent sessions to the RentBetter portal.
- **Integrated Property Portal**: A seamless, edge-to-edge WebView interface designed to provide a native feel while browsing the RentBetter website.
- **Smart Menu Bridge**: A dedicated ViewModel-driven logic layer that intercepts and simplifies the website's complex hamburger menu for optimized mobile navigation.
- **Adaptive Material 3 Interface**: A responsive UI built with Material Design 3 that ensures a consistent and vibrant experience across smartphones, tablets, and foldables.

## High-Level Technical Stack
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (Material Design 3)
- **Navigation**: Jetpack Navigation 3 (State-driven)
- **Adaptive Strategy**: Compose Material Adaptive Library
- **Architecture**: MVVM (ViewModel for state management and JavaScript-to-Native communication)
- **Asynchronous Tasks**: Kotlin Coroutines
- **Security**: EncryptedSharedPreferences (for secure credential storage)
- **Web Integration**: Android WebView with custom WebViewClient and WebChromeClient

## Implementation Steps
**Total Duration:** 1h 15m 59s

### Task_1_Setup: Initialize project dependencies, Material 3 theme, and Edge-to-Edge support.
- **Status:** COMPLETED
- **Updates:** Initialized project dependencies in libs.versions.toml and build.gradle.kts (Compose, Material 3, Security-Crypto, Navigation 3, Adaptive).
- **Acceptance Criteria:**
  - Project builds with required dependencies in libs.versions.toml and build.gradle.kts
  - Material 3 theme with vibrant colors is defined
  - Edge-to-Edge is enabled in MainActivity

### Task_2_SecureStorage_ViewModel: Implement secure credential storage and the ViewModel for app state.
- **Status:** COMPLETED
- **Updates:** Implemented SecureStorage using EncryptedSharedPreferences for encrypted credential storage.
- **Acceptance Criteria:**
  - EncryptedSharedPreferences implemented for credential storage
  - ViewModel created to manage login state and menu interaction logic
- **Duration:** 1m 10s

### Task_3_WebView_JSBridge: Integrate the WebView with JavaScript injection for automated login and menu handling.
- **Status:** COMPLETED
- **Updates:** Fixed hamburger menu issue by:
- **Acceptance Criteria:**
  - WebView displays RentBetter login page
  - JavaScript injection successfully automates login and handles the hamburger menu
  - Compose WebView integration is functional
- **Duration:** 2m 7s

### Task_4_AdaptiveIcon_Refinement: Refine the UI with Material 3 components and create an adaptive app icon.
- **Status:** COMPLETED
- **Updates:** Refined UI with Material 3 components (CenterAlignedTopAppBar, LinearProgressIndicator).
- **Acceptance Criteria:**
  - UI is responsive and follows M3 guidelines
  - Adaptive app icon is generated and applied
  - Vibrant color scheme is visible across the app
- **Duration:** 36m 20s

### Task_5_Run_Verify: Run the application and verify all features work as expected.
- **Status:** COMPLETED
- **Updates:** Removed debug logging (Logcat and JS console logs) and temporary diagnostic scripts.
Cleaned up WebViewViewModel by removing unused placeholder functions.
Verified that core functionality (automated login, hamburger menu bridge) remains stable.
- **Acceptance Criteria:**
  - App builds and runs without crashes
  - Automatic login works after first use
  - Hamburger menu bridge is functional
  - UI aligns with project requirements
- **Duration:** 36m 22s

