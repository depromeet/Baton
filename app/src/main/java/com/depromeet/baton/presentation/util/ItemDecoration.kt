package com.depromeet.baton.presentation.util

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.bds.utils.toPx

class TicketIteHorizontalDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val betweenSpacePx = BETWEEN_SPACE.toPx()
        val startEndSpacePx = START_END_SPACE.toPx()

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = startEndSpacePx
            outRect.right = betweenSpacePx
        } else if (parent.getChildAdapterPosition(view) == state.itemCount - 1) {
            outRect.right = startEndSpacePx
        } else {
            outRect.right = betweenSpacePx
        }
    }

    companion object {
        private const val BETWEEN_SPACE = 12
        private const val START_END_SPACE = 16
    }
}

class TicketItemVerticalDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val betweenSpacePx = BETWEEN_SPACE.toPx()
        val bottomSpacePx = BOTTOM_SPACE.toPx()

        if (parent.getChildAdapterPosition(view) % 2 == 0) {
            outRect.right = betweenSpacePx
        } else {
            outRect.left = betweenSpacePx
        }
        outRect.bottom = bottomSpacePx
    }

    companion object {
        private const val BETWEEN_SPACE = 8
        private const val BOTTOM_SPACE = 24
    }
}

class DetailImgDecoration: RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        outRect.right = 0
        outRect.left = 0

    }

}


