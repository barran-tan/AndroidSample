package com.barran.sample.constraint

import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.helper.widget.Flow
import androidx.transition.TransitionManager
import com.barran.sample.R
import com.barran.sample.databinding.ActivityConstraintLayoutV2Binding

/**
 * ConstraintLayout 2.0新增特性
 */
class TestConstraintLayout3Activity:AppCompatActivity() {
    private lateinit var binding:ActivityConstraintLayoutV2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraint_layout_v2)
        binding = ActivityConstraintLayoutV2Binding.bind(findViewById(R.id.root))
        initView()
    }

    private fun initView(){
        binding.buttonGroupOrientation.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.button_orientation_vertical -> Flow.VERTICAL
                    else -> Flow.HORIZONTAL
                }.let {
                    TransitionManager.beginDelayedTransition(binding.parentContainer)
                    binding.flow.setOrientation(it)
                }
            }
        }
        binding.buttonGroupWrapModes.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.button_wrap_mode_chain -> Flow.WRAP_CHAIN
                    R.id.button_wrap_mode_align -> Flow.WRAP_ALIGNED
                    else -> Flow.WRAP_NONE
                }.let {
                    TransitionManager.beginDelayedTransition(binding.parentContainer)
                    binding.flow.setWrapMode(it)
                }
            }
        }
        binding.buttonGroupVerticalChainStyle.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.button_vertical_chain_style_spread -> Flow.CHAIN_SPREAD
                    R.id.button_vertical_chain_style_spread_inside -> Flow.CHAIN_SPREAD_INSIDE
                    else -> Flow.CHAIN_PACKED
                }.let {
                    TransitionManager.beginDelayedTransition(binding.parentContainer)
                    binding.flow.setVerticalStyle(it)
                }
            }
        }
        binding.buttonGroupHorizontalChainStyle.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.button_horizontal_chain_style_spread -> Flow.CHAIN_SPREAD
                    R.id.button_horizontal_chain_style_spread_inside -> Flow.CHAIN_SPREAD_INSIDE
                    else -> Flow.CHAIN_PACKED
                }.let {
                    TransitionManager.beginDelayedTransition(binding.parentContainer)
                    binding.flow.setHorizontalStyle(it)
                }
            }
        }
        binding.seekbarHorizontalBias.apply {
            addSeekBarChangeListener(progressChanged = { _, value, _ -> binding.flow.setHorizontalBias(value / 100f) })
        }
        binding.seekbarVerticalBias.apply {
            addSeekBarChangeListener(progressChanged = { _, value, _ -> binding.flow.setVerticalBias(value / 100f) })
        }
        binding.seekbarHorizontalGap.apply {
            addSeekBarChangeListener(progressChanged = { _, value, _ -> binding.flow.setHorizontalGap(value) })
        }
        binding.seekbarVerticalGap.apply {
            addSeekBarChangeListener(progressChanged = { _, value, _ -> binding.flow.setVerticalGap(value) })
        }
        binding.buttonGroupHorizontalAlignments.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                TransitionManager.beginDelayedTransition(binding.parentContainer)
                when (checkedId) {
                    R.id.button_horizontal_alignment_end -> Flow.HORIZONTAL_ALIGN_END
                    R.id.button_horizontal_alignment_start -> Flow.HORIZONTAL_ALIGN_START
                    else -> Flow.HORIZONTAL_ALIGN_CENTER
                }.let {
                    binding.flow.setHorizontalAlign(it)
                }
            }
        }
        binding.buttonGroupVerticalAlignments.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                TransitionManager.beginDelayedTransition(binding.parentContainer)
                when (checkedId) {
                    R.id.button_vertical_alignment_top -> Flow.VERTICAL_ALIGN_TOP
                    R.id.button_vertical_alignment_bottom -> Flow.VERTICAL_ALIGN_BOTTOM
                    R.id.button_vertical_alignment_baseline -> Flow.VERTICAL_ALIGN_BASELINE
                    else -> Flow.VERTICAL_ALIGN_CENTER
                }.let {
                    binding.flow.setVerticalAlign(it)
                }
            }
        }

    }

}

inline fun SeekBar.addSeekBarChangeListener(
    crossinline startTrackingTouch: (p0: SeekBar?) -> Unit = {},
    crossinline stopTrackingTouch: (p0: SeekBar?) -> Unit = {},
    crossinline progressChanged: (p0: SeekBar?, p1: Int, p2: Boolean) -> Unit
) {
    setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            progressChanged(seekBar, progress, fromUser)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            startTrackingTouch(seekBar)
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            stopTrackingTouch(seekBar)
        }
    })
}


