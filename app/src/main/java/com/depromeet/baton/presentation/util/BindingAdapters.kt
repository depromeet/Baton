package com.depromeet.baton.presentation.util

import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import com.depromeet.baton.domain.model.FilterType
import com.depromeet.baton.presentation.ui.filter.getTicketKindFromCheckedId
import com.depromeet.baton.presentation.ui.filter.viewmodel.FilterViewModel
import com.depromeet.bds.component.BdsFilter
import com.google.android.material.chip.Chip

@BindingAdapter("viewModel", "itemType")
fun setFilterItemOnCheckedChanged(view: Chip, viewModel: ViewModel, itemType: FilterType) {
    view.setOnCheckedChangeListener { _, isChecked ->
        when (itemType) {
            FilterType.TicketKind -> {
                (viewModel as? FilterViewModel)
                    ?.setTicketKind(getTicketKindFromCheckedId(view.id) ?: return@setOnCheckedChangeListener, isChecked)
            }
        }
    }
}

@BindingAdapter("bds_text", "isSelected")
fun setBdsFilter(view: BdsFilter, text: String, isSelected: Boolean) {
    view.text = text
    view.isSelected = isSelected
}
