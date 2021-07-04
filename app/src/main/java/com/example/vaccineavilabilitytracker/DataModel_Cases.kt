package com.example.vaccineavilabilitytracker

data class DataModel_Cases(

    // string variable for center name.
    val stateName: String,


    // string variable for center opening time.

    val active : String,

    val recovered: String,

    val deaths: String,

    val cases: String,

    val todayActive : String,

    val todayRecovered: String,

    val todayDeaths: String,

    val todayCases: String

)