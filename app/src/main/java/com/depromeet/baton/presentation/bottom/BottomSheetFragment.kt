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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class BottomSheetFragment:
    BottomSheetDialogFragment() {

    private  var checkItemAdapter : BottomSheetAdapter<ItemBottomsheetCheckBinding>? =null
    private  var ItemAdapter : BottomSheetAdapter<ItemBottomsheetBinding>? =null

    val list by lazy {  arguments?.getParcelableArray("bottomList")?.toList() as List<BottomMenuItem>}

    private var mClickListener :OnItemClick? = null
    private val DURATION = 700L


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

        BottomSheetBehavior.from<View>(bottomSheet!!).apply {
            isFitToContents = true
            maxHeight =1000
            state = BottomSheetBehavior.STATE_DRAGGING
        }
        view.findViewById<TextView>(R.id.bottom_sheet_title_tv).text= arguments?.getString("bottomTitle")
        initAdapter()
    }

    private fun bottomDismiss(){
        CoroutineScope(Dispatchers.Main).launch {
            delay( DURATION )
            dialog?.dismiss()
        }
    }

    private fun onClickCheckItem(itemPos : Int){
        changeStatus(itemPos)
        mClickListener?.onSelectedItem( checkItemAdapter!!.currentList[itemPos],  itemPos)
        bottomDismiss()
    }

    private fun onClickDefaultItem(itemPos : Int){
        val changed = ItemAdapter!!.currentList.map { it.copy() }
        changed[itemPos].isChecked=true
        ItemAdapter!!.submitList(changed)
        mClickListener?.onSelectedItem( ItemAdapter!!.currentList[itemPos],  itemPos)
        bottomDismiss()
    }

    //selectedItem 변경
    fun changeStatus(itemPos: Int ){
        val oldPos:Int?= checkItemAdapter!!.oldCheckedPos
        val changed = checkItemAdapter!!.currentList.map { it.copy() }

        changed [itemPos].isChecked= true

        if(oldPos !==null &&oldPos!=itemPos)
            changed [oldPos].isChecked=false

        checkItemAdapter!!.submitList(changed)
        checkItemAdapter!!.oldCheckedPos = itemPos
    }

    private fun initAdapter(){
        when(arguments?.getInt("bottomType")){
            DEFAULT_ITEM_VIEW ->{
                ItemAdapter= BottomSheetAdapter(DEFAULT_ITEM_VIEW ){
                        selectedItem: Int-> onClickDefaultItem(selectedItem)
                }
                view?.findViewById<RecyclerView>(R.id.bottom_sheet_rv)?.adapter= ItemAdapter
                ItemAdapter?.submitList(list)

            }

            CHECK_ITEM_VIEW->{
                checkItemAdapter = BottomSheetAdapter( CHECK_ITEM_VIEW){
                        selectedItem: Int-> onClickCheckItem(selectedItem)
                }
                view?.findViewById<RecyclerView>(R.id.bottom_sheet_rv)?.adapter=checkItemAdapter
                checkItemAdapter?.submitList(list)

            }
        }
    }

    companion object{
        val DEFAULT_ITEM_VIEW = R.layout.item_bottomsheet
        val CHECK_ITEM_VIEW = R.layout.item_bottomsheet_check

        interface OnItemClick {
            fun onSelectedItem(selected: BottomMenuItem, pos: Int)
        }

        fun newInstance(title : String , list : List<BottomMenuItem> ,viewType: Int , selectedListener : OnItemClick) : BottomSheetDialogFragment{
            val args = Bundle().apply{
                putString("bottomTitle",title)
                putParcelableArray("bottomList", list.toTypedArray())
                putInt("bottomType",viewType)
            }
            val fragment = BottomSheetFragment()
            fragment.arguments = args
            fragment.mClickListener=selectedListener
            return fragment
        }
    }
}