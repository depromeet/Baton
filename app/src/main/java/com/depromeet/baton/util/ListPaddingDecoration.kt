package com.depromeet.baton.util

import android.graphics.Rect
import android.view.View
import androidx.annotation.Dimension
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.bds.utils.toPx

class ListPaddingDecoration(
    @Dimension(unit = Dimension.DP) private val paddingDp: Int
) : RecyclerView.ItemDecoration() {

    private val padding: Int get() = paddingDp.toPx()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val itemPosition = parent.getChildAdapterPosition(view)
        if (itemPosition == RecyclerView.NO_POSITION) {
            return
        }

        outRect.right = padding
    }
}