package ows.kotlinstudy.subway_application.extension

import android.content.Context
import androidx.annotation.Px


// 160 dpi -> 1인치에 160px이 존재 -> 1dp = 1px
// 320 dpi -> 1인치에 320px이 존재 -> 1dp = 2px
// px = dpi/160 * dp
// dp = px *160 / dpi
// 160*px = dpi*dp
// 160dpi : xdpi = ydp : zpx

@Px
fun Context.dip(dipValue: Float) = (dipValue*resources.displayMetrics.density).toInt()