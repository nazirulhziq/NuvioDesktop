package com.nuvio.app.core.ui

import androidx.compose.runtime.staticCompositionLocalOf

/** Provided on desktop so shared UI can offer a quit affordance. */
internal val LocalAppExitRequest = staticCompositionLocalOf<(() -> Unit)?> { null }
