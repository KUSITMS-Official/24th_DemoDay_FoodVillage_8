package com.example.foodvillage.schema

data class Store(
    var storeName: String? = null,
    var currentLatitude: Double? = null,
    var currentLongitude: Double? = null,
    var address: String? = null,
    var categoryNames: List<String>? = null,
    var distance: HashMap<String, Double>? = null,
    var storeImg: String? = null,
    var grade: Double? = null,
    var dibPeople: List<String>? = null,
    var reviewCnt: Int? = null,
    var productCnt: Int? = null,
    var phoneNumber: String? = null,
    var time: String? = null,
    var dayOff: String? = null
)
