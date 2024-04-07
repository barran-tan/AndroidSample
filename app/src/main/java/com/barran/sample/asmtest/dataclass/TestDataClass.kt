package com.barran.sample.asmtest.dataclass


class TestDataClass {

    fun test(){
        val ktData1_a = KtDataClass(1,"aa")
        val ktData1_b = ktData1_a.copy(1,"bb")

        println("1_a hashcode=${ktData1_a.hashCode()}  1_b hashcode=${ktData1_b.hashCode()}")
        println("1_a=$ktData1_a 1_b=$ktData1_b equals=${ktData1_a.equals(ktData1_b)}")

        val obj = Object()
        val ktData1_c = KtDataClass(1,"cc",obj)
        val ktData1_d = KtDataClass(1,"cc",obj)
        println("1_c hashcode=${ktData1_c.hashCode()}  1_d hashcode=${ktData1_d.hashCode()}")
        println("1_c=$ktData1_c 1_d=$ktData1_d equals=${ktData1_c.equals(ktData1_d)}")
    }

}