package com.barran.sample.compose.pages

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.barran.sample.App
import kotlin.math.roundToInt

@Composable
fun GesturePage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {

        SingleTap()

        CombineClick()

        DragGesture(Modifier.align(Alignment.CenterHorizontally))

        MultiGesture()
    }
}

@Composable
fun SingleTap() {
    Box(
        modifier = Modifier
            .size(width = 100.dp, height = 50.dp)
            .background(color = Color.Green)
            .clickable {
                toast("click box")
            }) {
        Text(text = "single click", modifier = Modifier.align(Alignment.Center))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CombineClick() {
    Box(
        modifier = Modifier
            .padding(top = 10.dp)
            .wrapContentSize()
            .background(color = Color.LightGray)
            .padding(start = 10.dp, top = 4.dp, end = 10.dp, bottom = 4.dp)
            .combinedClickable(
                onClickLabel = "click label",
                onLongClickLabel = "long click label",
                onLongClick = {
                    toast("long click box")
                },
                onDoubleClick = {
                    toast("double click box")
                },
                onClick = {
                    toast("click box")
                }
            )
    ) {
        Text(
            text = "support click, double click, long click",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun DragGesture(modifier: Modifier) {
    val offset = remember {
        mutableStateOf(0f)
    }
    val dragState = rememberDraggableState(onDelta = {
        offset.value = (offset.value + it).coerceIn(0f, 300.dp.value)
    })
    Box(
        modifier = modifier
            .padding(top = 10.dp)
            .size(width = 50.dp, height = 50.dp)
            .offset {
                IntOffset(offset.value.roundToInt(), 0)
            }
            .background(color = Color.Gray)
            .draggable(
                state = dragState,
                orientation = Orientation.Horizontal
            )
    ) {
        Text(text = "drag box", modifier = Modifier.align(Alignment.Center))
    }
}

// 1.6.0-alpha01
/*@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SwipeableSample() {
    val width = 96.dp
    val squareSize = 48.dp

    val swipeableState = rememberSwipeableState(0)
    val sizePx = with(LocalDensity.current) { squareSize.toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1) // Maps anchor points (in px) to states

    Box(
        modifier = Modifier
            .width(width)
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal
            )
            .background(Color.LightGray)
    ) {
        Box(
            Modifier
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .size(squareSize)
                .background(Color.DarkGray)
        )
    }
}*/

@Composable
fun MultiGesture() {
    // set up all transformation states
    var scale by remember { mutableStateOf(1f) }
    var rotation by remember { mutableStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scale *= zoomChange
        rotation += rotationChange
        offset += offsetChange
    }
    Box(
        Modifier
            .padding(top = 10.dp)
            // apply other transformations like rotation and zoom
            // on the pizza slice emoji
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                rotationZ = rotation,
                translationX = offset.x,
                translationY = offset.y
            )
            // add transformable to listen to multitouch transformation events
            // after offset
            .transformable(state = state)
            .background(Color.Blue)
            .fillMaxSize()
    )
}

private fun toast(toast: String) {
    Toast
        .makeText(App.context, toast, Toast.LENGTH_SHORT)
        .show()
}
