package com.carry1st_shop.screens

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.carry1st_shop.ui.theme.Purple40
import com.carry1st_shop.ui.theme.PurpleGrey80
import kotlin.math.PI
import kotlin.math.max

@Composable
fun Carry1stLoader() {
    Box(
        contentAlignment = Alignment.Center,
    ) {
        Carry1stCircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = Purple40,
            strokeWidth = 8.dp
        )
    }
}

fun DrawScope.drawCircularIndicator(
    startAngle: Float,
    sweep: Float,
    stroke: Stroke
) {
    // To draw this circle we need a rect with edges that line up with the midpoint of the stroke.
    // To do this we need to remove half the stroke width from the total diameter for both sides.
    val diameterOffset = stroke.width / 2
    val arcDimen = size.width - 2 * diameterOffset

    rotate(degrees = startAngle) {
        drawArc(
            brush = Brush.sweepGradient(
                colorStops = listOf(
                    0.0f to Purple40,
                    sweep / 360 to Color.Transparent,
                ).toTypedArray()
            ),
            startAngle = 0f,
            sweepAngle = sweep,
            useCenter = false,
            topLeft = Offset(diameterOffset, diameterOffset),
            size = Size(arcDimen, arcDimen),
            style = stroke
        )
    }
}

private fun DrawScope.drawIndeterminateCircularIndicator(
    startAngle: Float,
    strokeWidth: Dp,
    sweep: Float,
    stroke: Stroke
) {
    val strokeCapOffset = if (stroke.cap == StrokeCap.Butt) {
        0f
    } else {
        // Length of arc is angle * radius
        // Angle (radians) is length / radius
        // The length should be the same as the stroke width for calculating the min angle
        (180.0 / PI).toFloat() * (strokeWidth / (40.dp / 2)) / 2f
    }

    // Adding a stroke cap draws half the stroke width behind the start point, so we want to
    // move it forward by that amount so the arc visually appears in the correct place
    val adjustedStartAngle = startAngle + strokeCapOffset

    // When the start and end angles are in the same place, we still want to draw a small sweep, so
    // the stroke caps get added on both ends and we draw the correct minimum length arc
    val adjustedSweep = max(sweep, 0.1f)

    drawCircularIndicator(adjustedStartAngle, adjustedSweep, stroke)
}

@Composable
fun Carry1stCircularProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = ProgressIndicatorDefaults.circularColor,
    strokeWidth: Dp = ProgressIndicatorDefaults.CircularStrokeWidth,
    strokeCap: StrokeCap = ProgressIndicatorDefaults.CircularIndeterminateStrokeCap,
) {
    val stroke = with(LocalDensity.current) {
        Stroke(width = strokeWidth.toPx(), cap = strokeCap)
    }

    val transition = rememberInfiniteTransition(label = "")

    val startAngle = transition.animateFloat(
        0f,
        360f,
        infiniteRepeatable(
            animation = tween(
                durationMillis = RotationDuration,
                easing = LinearEasing
            )
        ), label = ""
    )
    Canvas(
        modifier
            .progressSemantics()
            .size(40.dp)
    ) {
        drawIndeterminateCircularIndicator(
            startAngle.value,
            strokeWidth,
            360f,
            stroke
        )
    }
}

// Each rotation is 1 and 1/3 seconds, but 1332ms divides more evenly
private const val RotationDuration = 1332




@Composable
fun LoadingScreen(message: String? = null, backgroundColor: Color = PurpleGrey80) {
    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(color = backgroundColor)
        ) {
            Carry1stLoader()
            Spacer(modifier = Modifier.height(10.dp))
            message?.let {
                Text(text = it, color = Color.White)
            }
        }
    }
}


@Preview
@Composable
fun PreviewLoader() {
    LoadingScreen()
}

