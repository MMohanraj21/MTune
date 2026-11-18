package com.opentune.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.opentune.R
import kotlinx.coroutines.delay

/** Plays once per launch of the app, not each time the screen is rebuilt. */
object Opening {
    var played = false
}

/** Deep obsidian backdrop matching the MTune logo. */
val OPENING_FIELD = Color(0xFF06070D)

/**
 * Displays the full-color MTune brand mark at launch with a gentle settle pulse,
 * then zooms and dissolves cleanly into the app.
 */
@Composable
fun OpeningReveal(onDive: (Float) -> Unit, onDone: () -> Unit) {
    val settle = remember { Animatable(0f) }
    val dive = remember { Animatable(0f) }
    val done by rememberUpdatedState(onDone)
    val report by rememberUpdatedState(onDive)

    LaunchedEffect(Unit) {
        settle.animateTo(1f, tween(SETTLE_MS, easing = CubicBezierEasing(0.2f, 0f, 0f, 1f)))
        delay(HOLD_MS)
        dive.animateTo(1f, tween(DIVE_MS, easing = CubicBezierEasing(0.4f, 0f, 0.2f, 1f))) { report(value) }
        report(1f)
        done()
    }

    val st = settle.value
    val d = dive.value
    val breathe = 0.94f + 0.06f * st
    val zoomScale = breathe * (1f + 0.85f * d * d)
    val bgAlpha = (1f - d * 1.15f).coerceIn(0f, 1f)
    val logoAlpha = (1f - d * 1.35f).coerceIn(0f, 1f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OPENING_FIELD.copy(alpha = bgAlpha)),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.mtune_logo_splash),
            contentDescription = "MTune",
            modifier = Modifier
                .size(210.dp)
                .graphicsLayer {
                    scaleX = zoomScale
                    scaleY = zoomScale
                    alpha = logoAlpha
                },
        )
    }
}

private const val SETTLE_MS = 480
private const val HOLD_MS = 140L
private const val DIVE_MS = 520
