package com.barran.sample.compose.pages

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.animation.addListener


@Composable
fun AnimPage() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {

        VisibleAnim()

        SizeAnim()

        LayoutSwitchAnim()

        PropertyAnim()
    }
}

@Composable
fun VisibleAnim() {
    val visible = remember {
        mutableStateOf(false)
    }
    val visible2 = remember {
        mutableStateOf(false)
    }
    Column(Modifier.fillMaxWidth()) {
        Row {
            Button(onClick = { visible.value = visible.value.not() }) {
                Text(text = "click to show visible anim")
            }

            // enter = fadeIn() + expandVertically()
            // exit = fadeOut() + shrinkVertically()
            AnimatedVisibility(visible = visible.value) {
                Text(text = "default anim")
            }
        }

        Row {
            Button(onClick = { visible2.value = visible2.value.not() }) {
                Text(text = "click to show visible anim")
            }

            AnimatedVisibility(
                visible = visible2.value,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(text = "fade in/out anim")
            }
        }
    }
}

@Composable
fun SizeAnim() {
    val expand = remember {
        mutableStateOf(false)
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "豫章故郡，洪都新府。星分翼轸，地接衡庐。襟三江而带五湖，控蛮荆而引瓯越。物华天宝，龙光射牛斗之墟；" + "人杰地灵，徐孺下陈蕃之榻。雄州雾列，俊采星驰。台隍枕夷夏之交，宾主尽东南之美。都督阎公之雅望，棨戟遥临；" + "宇文新州之懿范，襜帷暂驻。十旬休假，胜友如云；千里逢迎，高朋满座。腾蛟起凤，孟学士之词宗；紫电青霜，王将军之武库。" + "家君作宰，路出名区；童子何知，躬逢胜饯",
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .animateContentSize(),
            maxLines = if (expand.value) 20 else 1
        )

        Button(onClick = { expand.value = expand.value.not() }) {
            Text(text = if (expand.value) "收起" else "展开")
        }
    }
}

@Composable
fun LayoutSwitchAnim() {
    val switch = remember {
        mutableStateOf(false)
    }
    Button(onClick = { switch.value = switch.value.not() }) {
        Text(text = "切换")
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {

        Crossfade(targetState = switch.value, label = "switch") {
            if (it) {
                Text(
                    text = "A",
                    Modifier
                        .fillMaxSize()
                        .background(Color.Cyan),
                    textAlign = TextAlign.Center
                )
            } else {
                Text(
                    text = "B",
                    Modifier
                        .fillMaxSize()
                        .background(Color.Green),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun PropertyAnim() {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        UploadButton()
    }
}

@Composable
fun UploadButton() {

    val originWidth = 180.dp
    val circleSize = 48.dp
    var text by remember { mutableStateOf("Upload") }
    val textAlpha = remember { mutableStateOf(1.0f) }
    val backgroundColor = remember { mutableStateOf(Color.Blue) }
    val boxWidth = remember { mutableStateOf(originWidth) }
    val height = remember { mutableStateOf(circleSize) }

    val progressAlpha = remember { mutableStateOf(0.0f) }

    val progress = remember { mutableStateOf(0) }

    val endAnimatorSet = remember {
        val animatorSet = AnimatorSet()
        val widthAnimator = animatorOfDp(boxWidth, arrayOf(circleSize, originWidth))
        val centerAnimator = animatorOfFloat(progressAlpha, 1f, 0f)
        val textAnimator = animatorOfFloat(textAlpha, 0f, 1f)
        val colorAnimator = animatorOfColor(backgroundColor, arrayOf(Color.Blue, Color.Red))
        animatorSet.playTogether(widthAnimator, centerAnimator, textAnimator, colorAnimator)
        animatorSet.addListener(onStart = {
            text = "Success"
        })
        animatorSet
    }

    //上传进度动画
    val progressAnimator = remember {
        val animator = animatorOfInt(progress, 0, 100)
        animator.duration = 1000
        animator.addListener(onEnd = {
            endAnimatorSet.start()
        })
        animator
    }

    val uploadStartAnimator = remember {
        // 创建 AnimatorSet
        val animatorSet = AnimatorSet()
        // 按钮宽度变化动画
        val widthAnimator = animatorOfDp(boxWidth, arrayOf(originWidth, circleSize))
        // 文字消失动画
        val textAnimator = animatorOfFloat(textAlpha, 1f, 0.0f)
        // 按钮颜色动画
        val colorAnimator = animatorOfColor(backgroundColor, arrayOf(Color.Blue, Color.Gray))
        // 动画添加到 AnimatorSet
        animatorSet.playTogether(widthAnimator, textAnimator, colorAnimator)

        // 中间白色透明度变化动画
        val centerAlphaAnimator = animatorOfFloat(progressAlpha, 0.0f, 1f)
        animatorSet.playTogether(widthAnimator, textAnimator, colorAnimator, centerAlphaAnimator)

        animatorSet.addListener(onEnd = {
            progressAnimator.start()
        })

        animatorSet
    }

    Box(
        modifier = Modifier
            .padding(start = 10.dp, top = 10.dp)
            .width(180.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(circleSize / 2))
                .background(backgroundColor.value)
                .size(boxWidth.value, height.value)
                .clickable {
                    uploadStartAnimator.start()
                },
            contentAlignment = Alignment.Center,
        ) {
            
            Box(
                modifier = Modifier
                    .size(height.value)
                    .clip(ArcShape(progress.value))
                    .alpha(progressAlpha.value)
                    .background(Color.Blue)
            )

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .alpha(progressAlpha.value)
                    .background(Color.White)
            )

            Text(text = text, color = Color.White, modifier = Modifier.alpha(textAlpha.value))
        }
    }
}

fun animatorOfInt(state: MutableState<Int>, vararg values: Int): ValueAnimator {
    // 创建 ValueAnimator ，参数为传入的 values
    val animator = ValueAnimator.ofInt(*values)
    // 添加监听
    animator.addUpdateListener {
        // 更新 state 的 value 值
        state.value = it.animatedValue as Int
    }
    return animator
}

fun animatorOfFloat(state: MutableState<Float>, vararg values: Float): ValueAnimator {
    // 创建 ValueAnimator ，参数为传入的 values
    val animator = ValueAnimator.ofFloat(*values)
    // 添加监听
    animator.addUpdateListener {
        // 更新 state 的 value 值
        state.value = it.animatedValue as Float
    }
    return animator
}

fun animatorOfColor(state: MutableState<Color>, values: Array<Color>): ValueAnimator {
    val colors: IntArray = values.map { it.toArgb() }.toIntArray()
    // 创建 ValueAnimator ，参数为传入的 values
    val animator = ValueAnimator.ofArgb(*colors)
    // 添加监听
    animator.addUpdateListener {
        // 更新 state 的 value 值
        state.value = Color(it.animatedValue as Int)
    }
    return animator
}

fun animatorOfDp(state: MutableState<Dp>, values: Array<Dp>): ValueAnimator {
    val dps: FloatArray = values.map { it.value }.toFloatArray()
    // 创建 ValueAnimator ，参数为传入的 values
    val animator = ValueAnimator.ofFloat(*dps)
    // 添加监听
    animator.addUpdateListener {
        // 更新 state 的 value 值
        state.value = Dp(it.animatedValue as Float)
    }
    return animator
}

class ArcShape(private val progress: Int) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            moveTo(size.width / 2f, size.height / 2f)
            arcTo(Rect(0f, 0f, size.width, size.height), -90f, progress / 100f * 360f, false)
            close()
        }
        return Outline.Generic(path)
    }
}
