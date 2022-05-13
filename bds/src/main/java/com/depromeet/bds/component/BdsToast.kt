package com.depromeet.bds.component
import android.content.Context
import android.content.res.Resources
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast


fun Context.BdsToast( message: String ): Toast {
    val view =  LayoutInflater.from(this).inflate(com.depromeet.bds.R.layout.bds_component_toast, null)
    view.findViewById<TextView>(com.depromeet.bds.R.id.tvSample).text=message
    return Toast(this).apply {
        setGravity(Gravity.BOTTOM or Gravity.CENTER, 0, 24.toPx())
        duration = Toast.LENGTH_SHORT
        this.view = view
    }
}

//특정 위치 기준 16px 위에 올리기
// 기준값의 위치값  pos로 받는다
fun Context.BdsToast(message: String , pos : Int): Toast {
    val view =  LayoutInflater.from(this).inflate(com.depromeet.bds.R.layout.bds_component_toast, null)
    view.findViewById<TextView>(com.depromeet.bds.R.id.tvSample).text=message

    val offset = bottomPos(pos)+16.toPx()
    return Toast(this).apply {
        setGravity(Gravity.BOTTOM or Gravity.CENTER, 0, offset)
        duration = Toast.LENGTH_SHORT
        this.view = view
    }
}

private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
private fun bottomPos(yPos: Int) : Int = ( Resources.getSystem().displayMetrics.heightPixels).toInt()-yPos