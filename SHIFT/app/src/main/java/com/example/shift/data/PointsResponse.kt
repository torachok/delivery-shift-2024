package com.example.shift.data

data class PointsResponse (
    val success : Boolean,
    val points : List<DeliveryPoints>

)

data class DeliveryPoints(
    val id : String,
    val name : String,
    val latitude : Float,
    val longitude : Float
)