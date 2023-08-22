package com.barran.sample.mdtest

import org.junit.Test

class KtTest {

    @Test
    fun testForeach(){
        val list = 0 until 10

        println("test return@forEach")
        list.forEach {
            if(it == 6){
                return@forEach
            }
            println("forEach $it")
        }

        println("test return@forEach2")
        for (i in list) run forEach@{
            if (i == 6) {
                return@forEach
            }
            println("forEach $i")
        }

        println("test return@loop")
        run loop@{
            list.forEach {
                if (it == 6) {
                    return@loop
                }
                println("forEach $it")
            }
        }

        println("test return")
        list.forEach {
            if(it == 6){
                return
            }
            println("forEach $it")
        }

        println("test end")
    }

}