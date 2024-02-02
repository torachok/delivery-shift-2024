package com.example.shift.data

data class TypeResponse (
    val success : Boolean,
    val packages : List<TypePackages>
)

data class TypePackages(
    val id : String,
    val name : String,
    val length : Int,
    val width : Int,
    val weight : Int,
    val height : Int
)