/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.barran.sample.constraint

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.helper.widget.Carousel
import com.barran.sample.R
import com.google.android.material.card.MaterialCardView


val carouselTypeList = listOf(0, 1, 2, 3, 4, 5, 6)

class CarouselHelperActivity : AppCompatActivity() {

    private val logTag = "carousel2"

    private var testType = 0

    private var tvLabel: TextView? = null

    private val colors = intArrayOf(
        Color.parseColor("#ffd54f"),
        Color.parseColor("#ffca28"),
        Color.parseColor("#ffc107"),
        Color.parseColor("#ffb300"),
        Color.parseColor("#ffa000"),
        Color.parseColor("#ff8f00"),
        Color.parseColor("#ff6f00"),
        Color.parseColor("#c43e00")
    )

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
        testType = intent.getIntExtra("testType", 0)
        when (testType) {
            1 -> {
                setContentView(R.layout.demo_010_carousel)
            }

            2 -> {
                setContentView(R.layout.demo_020_carousel)
            }
            3->{
                setContentView(R.layout.demo_030_carousel)
            }
            4->{
                setContentView(R.layout.demo_040_carousel)
            }
            5->{
                setContentView(R.layout.demo_050_carousel)
                tvLabel = findViewById(R.id.label)
            }
            6->{
                setContentView(R.layout.demo_060_carousel)
                tvLabel = findViewById(R.id.label)
            }
            else -> {
                setContentView(R.layout.activity_carousel_helper)
            }
        }
        setupCarousel()
    }


    private fun setupCarousel() {
        val carousel = findViewById<Carousel>(R.id.carousel) ?: return

        carousel.setAdapter(object : Carousel.Adapter {
            override fun count(): Int {
                return when (testType) {
                    in 1..5 -> {
                        images.size
                    }

                    6 -> {
                        9
                    }

                    else -> {
                        colors.size
                    }
                }

            }

            override fun populate(view: View, index: Int) {
                Log.v(logTag, "populate index=$index")
                when (testType) {
                    in 1..5 -> {
                        (view as ImageView).setImageResource(images[index])
                    }

                    6 -> {
                        (view as TextView).text = "#$index"
                    }

                    else -> {
                        if (view is MaterialCardView) {
                            view.setBackgroundColor(colors[index])
                        }
                    }
                }
            }

            override fun onNewItem(index: Int) {
                Log.v(logTag, "onNewItem $index")
                when (testType) {
                    6 -> {
                        tvLabel?.text = "#$index"
                    }
                }
            }
        })
    }
}