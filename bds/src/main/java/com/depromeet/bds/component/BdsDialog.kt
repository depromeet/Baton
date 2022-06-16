package com.depromeet.bds.component

import android.app.AlertDialog
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.depromeet.bds.R

class BdsDialog  constructor(
    context: Context,
    type : DialogType,
) {
    private var alertDialog : AlertDialog? = null
    private val layoutInflater = LayoutInflater.from(context)
    val view = layoutInflater.inflate(type.view, null)
    init {
        alertDialog = AlertDialog.Builder(context, R.style.BdsDialogStyle)
            .setView(view)
            .create()
    }

    fun setSingleDialog(onClickConfirm :()-> Unit){
        view?.findViewById<Button>(R.id.bds_dialog_single_confirm_btn)?.visibility=View.VISIBLE
        view?.findViewById<Button>(R.id.bds_dialog_single_confirm_btn)?.setOnClickListener { onClickConfirm() }
    }

    fun setHorizonDialog(onClickConfirm :()-> Unit , onClickCancel :()-> Unit,confirmText : String?=null, cancelText:String?=null)  {
        view?.findViewById<LinearLayout>(R.id.bds_dialog_two_horizon)?.visibility=View.VISIBLE
        view?.findViewById<Button>(R.id.bds_dialog_horizon_confirm_btn)?.setOnClickListener { onClickConfirm() }
        view?.findViewById<Button>(R.id.bds_dialog_horizon_cancel_btn)?.setOnClickListener {
            onClickCancel()
            alertDialog?.dismiss()
        }
        confirmText?.let {   view?.findViewById<Button>(R.id.bds_dialog_horizon_confirm_btn)?.setText(confirmText) }
        cancelText?.let {   view?.findViewById<Button>(R.id.bds_dialog_horizon_cancel_btn)?.setText(confirmText) }
    }

    fun setVerticalDialog(onClickConfirm :()-> Unit, onClickCancel :()-> Unit, confirmText : String?=null, cancelText:String?=null){
        view?.findViewById<GridLayout>(R.id.bds_dialog_two_vertical)?.visibility=View.VISIBLE
        view?.findViewById<Button>(R.id.bds_dialog_vertical_confirm_btn)?.setOnClickListener { onClickConfirm() }
        view?.findViewById<Button>(R.id.bds_dialog_vertical_cancel_btn)?.setOnClickListener {
            onClickCancel()
            alertDialog?.dismiss()
        }
        confirmText?.let {   view?.findViewById<Button>(R.id.bds_dialog_vertical_confirm_btn)?.setText(confirmText) }
        cancelText?.let {   view?.findViewById<Button>(R.id.bds_dialog_vertical_cancel_btn)?.setText(confirmText) }
    }

    fun setImage(res: Int){
        view?.findViewById<ImageView>(R.id.bds_dialog_iv)?.setImageResource(res)
    }

    fun setTitle(title : String){
        view?.findViewById<TextView>(R.id.bds_dialog_title_tv)?.visibility=View.VISIBLE
        view?.findViewById<TextView>(R.id.bds_dialog_title_tv)?.setText(title)
    }

    fun setContent(content : String){
        view?.findViewById<TextView>(R.id.bds_dialog_content_tv)?.visibility=View.VISIBLE
        view?.findViewById<TextView>(R.id.bds_dialog_content_tv)?.setText(content)
    }

    fun show(){
        alertDialog?.show()
    }

    fun dismiss(){
        alertDialog?.dismiss()
    }

}

enum class DialogType(val view : Int){
    PRIMARY (R.layout.bds_component_dialog),
    SECONDARY(R.layout.bds_component_secondary_dialog)
}