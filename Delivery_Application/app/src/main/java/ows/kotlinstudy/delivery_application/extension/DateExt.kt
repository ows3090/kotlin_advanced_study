package ows.kotlinstudy.delivery_application.extension

import java.text.SimpleDateFormat
import java.util.*


private val dateFormat = SimpleDateFormat("MM.dd", Locale.KOREA)

fun Date.toReadableDateString(): String = dateFormat.format(this)