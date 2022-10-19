package com.depromeet.baton.presentation.ui.home.view

import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class StickyHeaderItemDecoration(
    private val sectionCallback: SectionCallback
) : RecyclerView.ItemDecoration() {


    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        val topChild = parent.getChildAt(0) ?: return

        val topChildPosition = parent.getChildAdapterPosition(topChild)

        val currentHeader = sectionCallback.getHeaderLayoutView(parent, topChildPosition) ?: return

        fixLayoutSize(parent, currentHeader)

        currentHeader.draw(c)
    }

    private fun fixLayoutSize(parent: ViewGroup, view: View) {

        val widthSpec =
            View.MeasureSpec.makeMeasureSpec(
                parent.width,
                View.MeasureSpec.EXACTLY
            )

        val heightSpec = View.MeasureSpec.makeMeasureSpec(
            parent.height,
            View.MeasureSpec.AT_MOST
        )

        val childWidth: Int = ViewGroup.getChildMeasureSpec(
            widthSpec,
            0,
            view.width
        )
        val childHeight: Int = ViewGroup.getChildMeasureSpec(
            heightSpec,
            0,
            view.layoutParams.height
        )
        view.measure(
            childWidth,
            childHeight
        )

        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    }

    interface SectionCallback {
        fun getHeaderLayoutView(list: RecyclerView, position: Int): View?
    }
}


