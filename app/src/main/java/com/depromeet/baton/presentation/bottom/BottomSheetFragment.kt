package com.depromeet.baton.presentation.bottom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ItemBottomsheetBinding
import com.depromeet.baton.databinding.ItemBottomsheetCheckBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment (
    val title : String,
    var list: MutableList<BottomMenuItem<String>>,
    var BottomSheetItem : Int,
    val itemClick: (Int) -> Unit) :
    BottomSheetDialogFragment() {

    private  var checkItemAdapter : BottomSheetAdapter<ItemBottomsheetCheckBinding>? =null
    private  var ItemAdapter : BottomSheetAdapter<ItemBottomsheetBinding>? =null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, com.depromeet.bds.R.style.BdsBottomSheetDialogTheme);

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragment_bottomsheet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from<View>(bottomSheet!!)
        behavior.state = BottomSheetBehavior.STATE_DRAGGING
        view.findViewById<TextView>(R.id.bottom_sheet_title_tv).text= title
        initAdapter()

    }

    private fun onClickCheckItem(itemPos : Int){
        itemClick(itemPos)
        val oldPos:Int?= checkItemAdapter!!.oldCheckedPos
        val changed = checkItemAdapter!!.currentList.map{it.copy()}
        changed [itemPos].isChecked=true
        if(oldPos !==null)
            changed [oldPos].isChecked=false
        checkItemAdapter!!.submitList(changed.toMutableList())
        checkItemAdapter!!.oldCheckedPos = itemPos

    }

    private fun onClickDefaultItem(itemPos : Int){
        itemClick(itemPos)
        dialog?.dismiss()
    }

    private fun initAdapter(){
        when(BottomSheetItem ){
            DEFAULT_ITEM_VIEW ->{
                ItemAdapter= BottomSheetAdapter(DEFAULT_ITEM_VIEW ){
                        selectedItem: Int-> onClickDefaultItem(selectedItem)
                }
                view?.findViewById<RecyclerView>(R.id.bottom_sheet_rv)?.adapter= ItemAdapter
                ItemAdapter?.submitList(list.map { it.copy() }.toMutableList())

            }

            CHECK_ITEM_VIEW->{
                checkItemAdapter = BottomSheetAdapter( CHECK_ITEM_VIEW){
                        selectedItem: Int-> onClickCheckItem(selectedItem)
                }
                view?.findViewById<RecyclerView>(R.id.bottom_sheet_rv)?.adapter=checkItemAdapter
                checkItemAdapter?.submitList(list.map { it.copy() }.toMutableList())

            }
        }
    }

    companion object{
        val DEFAULT_ITEM_VIEW = R.layout.item_bottomsheet
        val CHECK_ITEM_VIEW = R.layout.item_bottomsheet_check
    }
}