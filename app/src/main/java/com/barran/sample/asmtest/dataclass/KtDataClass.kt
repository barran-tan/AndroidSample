package com.barran.sample.asmtest.dataclass


data class KtDataClass(
    var aaa: Int = 1,
    var bbb: String = "2",
    var ccc: Object = Object()
) /*: AbsDataClass()*/ {

    /*override fun getObjects(): Array<Any> {
        return arrayOf(aaa, bbb, ccc)
    }*/
}