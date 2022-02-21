package ows.kotlinstudy.camera_application.extensions

import android.content.res.Resources

internal fun Float.fromDpToPx() : Int{

    // DPI : Dot per Inch, 160dpi = mdpi(기본), 160dpi일 때 1dp = 1px
    // 240dpi 일때 3dp를 px로 변환하면?
    // 1dp = 240/160 = 1.5px, 3dp = 4.5px
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}