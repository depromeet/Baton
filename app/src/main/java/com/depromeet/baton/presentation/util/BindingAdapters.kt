package com.depromeet.baton.presentation.util

import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import com.depromeet.baton.domain.model.FilterType
import com.depromeet.baton.presentation.ui.filter.getTicketKindFromCheckedId
import com.depromeet.baton.presentation.ui.filter.viewmodel.FilterViewModel
import com.depromeet.bds.R
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

@BindingAdapter("viewModel", "filterType")
fun setFilterTypeOnCheckedChanged(view: Chip, viewModel: ViewModel, filterType: FilterType) {
    view.setOnCheckedChangeListener { _, _ ->
        when (filterType) {
            FilterType.TicketKind -> {
                view.isChecked = (viewModel as? FilterViewModel)?.isTicketKindFiltered?.value ?: false
            }
        }
        //chip의 closeIcon 색상변경 반영이 느려서 여기서 색상변경 코드를 한번 더 작성해주어야하는 문제가,,
        if (view.isChecked) view.setCloseIconTintResource(R.color.blue50)
        else view.setCloseIconTintResource(R.color.gy60)
    }
}
