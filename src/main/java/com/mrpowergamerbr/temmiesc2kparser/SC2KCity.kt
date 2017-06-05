package com.mrpowergamerbr.temmiesc2kparser

import com.mrpowergamerbr.temmiesc2kparser.utils.Array2D

class SC2KCity {
    var cityName: String? = null; // City name, can be null (if the city name is not found)
    var money: Double? = 0.0; // City's money supply
    var miscellaneous: HashMap<Int, Int> = HashMap<Int, Int>();
    var labels: HashMap<Int, String> = HashMap<Int, String>();
    val buildings = Array2D<String>(128, 128) { x, y -> "$x $y" }
}