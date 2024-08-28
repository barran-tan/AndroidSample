package com.barran.sample.constraint

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.helper.widget.Carousel
import androidx.constraintlayout.utils.widget.ImageFilterView
import com.barran.sample.R
import com.barran.sample.databinding.ActivityMotionCarouselBinding

class TestMotionCarouselActivity : AppCompatActivity() {

    private val logTag = "carousel"

    private lateinit var binding: ActivityMotionCarouselBinding

    private val images = listOf(
        R.drawable.bg,
        R.drawable.flower,
        R.drawable.lake,
        R.drawable.mountain_water,
        R.drawable.mountain2,
        R.drawable.rain,
        R.drawable.rain_2,
        R.drawable.road,
        R.drawable.starry_sky_universe,
        R.drawable.universe
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motion_carousel)
        binding = ActivityMotionCarouselBinding.bind(findViewById(R.id.root))

        initView()
    }

    private fun initView() {
        binding.carousel.setAdapter(object : Carousel.Adapter {
            override fun count(): Int {
                return images.size
            }

            override fun populate(view: View, index: Int) {
                Log.v(logTag, "populate tag=${view.tag} index=$index")
                (view as ImageFilterView).setImageResource(images[index])
            }

            override fun onNewItem(index: Int) {
                // called when an item is set
                Log.v(logTag, "onNewItem $index")
            }
        })

    }
}