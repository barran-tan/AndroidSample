package com.barran.sample.compose.pages

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AnimPage2() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {

        AnimAsState()

        UploadBtn2()

        UploadBtn3()
    }
}

@Composable
fun AnimAsState() {
    var anim by remember {
        mutableStateOf(false)
    }
    val paddingStart = animateDpAsState(if (anim) 100.dp else 10.dp, label = "paddingStart") {
        // 动画监听
        if (it == 100.dp) {
            anim = false
        }
    }
    val color =
        animateColorAsState(targetValue = if (anim) Color.Blue else Color.Gray, label = "bgColor")

    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "test animAsState",
            modifier = Modifier
                .padding(
                    start = paddingStart.value,
                    top = 10.dp
                )
                .background(color.value)
                .clickable {
                    anim = !anim
                })
    }
}

@Composable
fun UploadBtn2() {
    val originWidth = 180.dp
    val circleSize = 48.dp
    var uploadState by remember { mutableStateOf(UploadState.Normal) }
    var text by remember { mutableStateOf("Upload") }

    var textAlphaValue = 1f
    var backgroundColorValue = Color.Blue
    var boxWidthValue = originWidth
    var progressAlphaValue = 0f
    var progressValue = 0

    when (uploadState) {
        // 默认状态不处理
        UploadState.Normal -> {}
        // 开始上传
        UploadState.Start -> {
            // 文字透明度变为0
            textAlphaValue = 0f
            // 按钮背景颜色变为灰色
            backgroundColorValue = Color.Gray
            // 按钮宽度变为圆的宽度
            boxWidthValue = circleSize
            // 中间进度的透明度变为 1
            progressAlphaValue = 1f
        }
        // 上传中状态
        UploadState.Uploading -> {
            textAlphaValue = 0f
            backgroundColorValue = Color.Gray
            boxWidthValue = circleSize
            progressAlphaValue = 1f
            // 进度值变为 100
            progressValue = 100
        }
        // 上传完成
        UploadState.Success -> {
            // 文字透明度变为 1
            textAlphaValue = 1f
            // 颜色变为红色
            backgroundColorValue = Color.Red
            // 按钮宽度变化默认时的原始宽度
            boxWidthValue = originWidth
            // 进度透明度变为 0f
            progressAlphaValue = 0f
        }
    }

    val textAlpha by animateFloatAsState(textAlphaValue, label = "textAlpha")
    val backgroundColor by animateColorAsState(backgroundColorValue, label = "backgroundColor")
    val boxWidth by animateDpAsState(boxWidthValue, label = "boxWidth") {
        if (uploadState == UploadState.Start) {
            uploadState = UploadState.Uploading
        }
    }
    val progressAlpha by animateFloatAsState(progressAlphaValue, label = "progressAlpha")
    val progress by animateIntAsState(progressValue, label = "progress") {
        if (uploadState == UploadState.Uploading) {
            uploadState = UploadState.Success
            // 文字内容修改为 Success
            text = "Success"
        }
    }

    // 界面布局
    Box(
        modifier = Modifier
            .padding(start = 10.dp, top = 10.dp)
            .width(originWidth),
        contentAlignment = Alignment.Center
    ) {
        // 按钮
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(circleSize / 2))
                .background(backgroundColor)
                .size(boxWidth, circleSize)
                .clickable {
                    // 点击时修改状态为开始上传
                    uploadState = UploadState.Start
                },
            contentAlignment = Alignment.Center,
        ) {
            // 进度
            Box(
                modifier = Modifier
                    .size(circleSize)
                    .clip(ArcShape(progress))
                    .alpha(progressAlpha)
                    .background(Color.Blue)
            )
            // 白色蒙版
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .alpha(progressAlpha)
                    .background(Color.White)
            )
            // 文字
            Text(text, color = Color.White, modifier = Modifier.alpha(textAlpha))
        }
    }

}

enum class UploadState {
    Normal,
    Start,
    Uploading,
    Success
}

// 使用animateValueAsState自定义动画实现
@Composable
fun UploadBtn3() {
    val originWidth = 180.dp
    val circleSize = 48.dp
    var uploadState by remember { mutableStateOf(UploadState.Normal) }
    var text by remember { mutableStateOf("Upload") }


    // 根据状态创建 UploadValue 实体
    val uploadValue = when (uploadState) {
        UploadState.Normal -> UploadValue(1f, originWidth, 0, 0f)
        UploadState.Start -> UploadValue(0f, circleSize, 0, 1f)
        UploadState.Uploading -> UploadValue(0f, circleSize, 100, 1f)
        UploadState.Success -> UploadValue(1f, originWidth, 100, 0f)
    }

    // 根据状态创建背景颜色
    val backgroundColorValue = when (uploadState) {
        UploadState.Normal -> Color.Blue
        UploadState.Start -> Color.Gray
        UploadState.Uploading -> Color.Gray
        UploadState.Success -> Color.Red
    }

    // 创建 UploadValue 的 State
    val upload by animateUploadAsState(uploadValue) {
        // 监听动画完成修改状态
        if (uploadState == UploadState.Start) {
            uploadState = UploadState.Uploading
        } else if (uploadState == UploadState.Uploading) {
            uploadState = UploadState.Success
            text = "Success"
        }
    }
    val backgroundColor by animateColorAsState(backgroundColorValue, label = "backgroundColor")


    Box(
        modifier = Modifier
            .padding(start = 10.dp, top = 10.dp)
            .width(originWidth),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(circleSize / 2))
                .background(backgroundColor)
                // 替换为使用 upload.boxWidth
                .size(upload.boxWidth, circleSize)
                .clickable {
                    uploadState = UploadState.Start
                },
            contentAlignment = Alignment.Center,
        ) {
            Box(
                // 替换为使用 upload.progress
                modifier = Modifier
                    .size(circleSize)
                    .clip(ArcShape(upload.progress))
                    // 替换为使用 upload.progressAlpha
                    .alpha(upload.progressAlpha)
                    .background(Color.Blue)
            )
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    // 替换为使用 upload.progressAlpha
                    .alpha(upload.progressAlpha)
                    .background(Color.White)
            )
            // 替换为使用 upload.textAlpha
            Text(text, color = Color.White, modifier = Modifier.alpha(upload.textAlpha))
        }
    }

}

data class UploadValue(val textAlpha : Float, val boxWidth : Dp, val progress:Int, val progressAlpha:Float){
    companion object
}

val UploadValue.Companion.VectorConverter: TwoWayConverter<UploadValue, AnimationVector4D>
    get() = UploadToVector


private val UploadToVector: TwoWayConverter<UploadValue, AnimationVector4D> = TwoWayConverter(
    convertToVector = { AnimationVector4D(it.textAlpha,  it.boxWidth.value, it.progress.toFloat(), it.progressAlpha) },
    convertFromVector = { UploadValue(it.v1, Dp(it.v2), it.v3.toInt(), it.v4) }
)

@Composable
fun animateUploadAsState(
    targetValue: UploadValue,
    finishedListener: ((UploadValue) -> Unit)? = null
): State<UploadValue> {
    return animateValueAsState(
        targetValue,
        UploadValue.VectorConverter,
        finishedListener = finishedListener, label = "animateUpload"
    )
}