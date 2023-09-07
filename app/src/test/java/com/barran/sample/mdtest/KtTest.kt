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

    @Test
    fun int32To64(){
        val intValue = -1381797423
        val hex = Integer.toHexString(intValue)
        println("hex of $intValue is $hex")
        val longValue = java.lang.Long.parseLong(hex,16)
        println("parse long from $hex to $longValue")

        println("parse long to Int ${longValue.toInt()}")
        println("parse long to UInt ${longValue.toUInt()}")
        println("parse int to UInt ${intValue.toUInt()}")
        println("parse int to java Long ${Integer.toUnsignedLong(intValue)}")
    }

    @Test
    fun int2UInt(){
        val intValue = 0
        println("parse int to UInt ${intValue.toUInt()}")
    }
}