package com.shiva.myhealth.utility
fun String.parseInt(string: String): Int{
    var str=string
    if (string.contains('.')){
        str=str.filter { it!='.' }
    }
    if (string.contains(',')){
        str=str.filter { it!=',' }
    }
    return  str.toInt()
}

    fun String.parseFloat(string: String): Float{
        var wholePart:Int
        var fractionalPart:Int
        if (string.contains('.')){
            wholePart=string.split('.')[0].toInt()
            fractionalPart=string.split('.')[1].toInt()
            return wholePart.toFloat()+fractionalPart/10
        }
        else if (string.contains(',')){
            wholePart=string.split(',')[0].toInt()
            fractionalPart=string.split(',')[1].toInt()
            return wholePart.toFloat()+fractionalPart/10
        }
        else return  string.toFloat()
    }
