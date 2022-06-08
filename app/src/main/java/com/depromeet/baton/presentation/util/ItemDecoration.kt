package com.depromeet.baton.presentation.util

import android.R.attr.spacing
import android.R.attr.windowMinWidthMajor
import android.graphics.Rect
import android.util.Log
import android.view.View
import android.widget.GridView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.bds.utils.toDp
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

        super.getItemOffsets(outRect, view, parent, state)
        outRect.setEmpty()
        val position = parent.getChildAdapterPosition(view).takeIf { it != RecyclerView.NO_POSITION } ?: run {
            return
        }
        val space = BETWEEN_SPACE.toDp()

        val layoutManager = parent.layoutManager as? GridLayoutManager ?: return
        val k = layoutManager.spanSizeLookup.getSpanIndex(position,4) % SPAN_COUNT
        outRect.left = ( k * space / SPAN_COUNT).toDp()
        outRect.right = (space - (k + 1) * space / SPAN_COUNT).toDp()
        outRect.bottom = BOTTOM_SPACE.toDp()


    }


    companion object {
        private const val BETWEEN_SPACE :Float = 6.5F
        private const val BOTTOM_SPACE = 16
        private const val SPAN_COUNT=4
    }
}
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


