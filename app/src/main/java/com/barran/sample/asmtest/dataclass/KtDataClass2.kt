package com.barran.sample.asmtest.dataclass;

data class KtDataClass2(
    val byte: Byte,
    val aaa: Int,
    val bbb: Short,
    val ccc: Long,
    val ddd: Double,
    val eee: Float,
    val fff: String,
    val ggg: Object,
    val hhh: Char,
    val xxx: Boolean,
    val yyy: Array<Object>,
    val zzz: Array<Int>
) /*: AbsDataClass()*/ {

    /*override fun getObjects(): Array<Any> {
        return arrayOf(aaa, bbb, ccc, ddd, eee, fff, ggg)
    }*/
}
