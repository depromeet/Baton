package com.depromeet.baton.presentation.bottom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.baton.R
import com.depromeet.baton.presentation.bottom.BottomSheetAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import timber.log.Timber
import kotlin.reflect.jvm.internal.impl.util.Check

class BottomSheetFragment (
    var list: MutableList<CheckItem<String>>,
    val itemClick: (Int) -> Unit) :
    BottomSheetDialogFragment() {
    val TAG = "BOTTOM_SHEET"
    private  var listAdapter: BottomSheetAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, com.depromeet.bds.R.style.BdsBottomSheetDialogTheme);
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

        initAdapter()
    }

    fun listItemClicked(itemPos : Int){
        itemClick(itemPos)

        val oldPos:Int?= listAdapter!!.oldCheckedPos
        val changed = listAdapter!!.currentList.map{it.copy()}

        changed [itemPos].isChecked=true
        if(oldPos !==null)
            changed [oldPos].isChecked=false

        listAdapter!!.submitList(changed.toMutableList())
        listAdapter!!.oldCheckedPos = itemPos

       // dialog?.dismiss()
    }

    private fun initAdapter(){
        listAdapter = BottomSheetAdapter(){
                selectedItem: Int-> listItemClicked(selectedItem)
        }
        setListItem(list)
        view?.findViewById<RecyclerView>(R.id.bottom_sheet_rv)?.adapter=listAdapter
    }

    fun setListItem(list : MutableList<CheckItem<String>>){
        listAdapter?.submitList(list.map { it.copy() }.toMutableList())
    }
}