package com.barran.sample.compose

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.barran.sample.compose.pages.AnimPage
import com.barran.sample.compose.pages.Entry
import com.barran.sample.compose.pages.GesturePage
import com.barran.sample.compose.pages.MainPage


class NaviPages {

    companion object{
        const val NAVI_PAGE_MAIN = "main"
        const val NAVI_PAGE_GESTURE = "gesture"
        const val NAVI_PAGE_ANIM = "anim"
    }

    lateinit var navController : NavHostController
        private set

    val entryList = mutableListOf<Entry>()
    init {
        entryList.add(Entry("gesture test") {
            navController.navigate(NAVI_PAGE_GESTURE)
        })
        entryList.add(Entry("anim test") {
            navController.navigate(NAVI_PAGE_ANIM)
        })
    }

    @Composable
    fun StartNaviPages() {

        navController = rememberNavController()

        NavHost(navController = navController, startDestination = NAVI_PAGE_MAIN) {
            composable(NAVI_PAGE_MAIN){
                MainPage(entryList)
            }
            composable(NAVI_PAGE_GESTURE) {
                GesturePage()
            }
            composable(NAVI_PAGE_ANIM) {
                AnimPage()
            }
        }
    }

}
