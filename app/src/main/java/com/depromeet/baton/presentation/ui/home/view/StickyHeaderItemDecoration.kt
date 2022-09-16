package com.depromeet.baton.presentation.ui.home.view

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.baton.R
import com.depromeet.baton.presentation.ui.filter.view.FilterChipFragment

class StickyHeaderItemDecoration(
    private val sectionCallback: SectionCallback //SectionCallback 상속한 익명객체를 아이템데코레이션 만들떄 넣기
) : RecyclerView.ItemDecoration() {


/*   recyclerView가 그려진 뒤에 호출 된다.
    그래서 RecyclerView 위에 그릴 수 있다.
    이 점을 이용해서 우리는 상단에 Sticky Header View를 그릴 것이다.*/

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
         super.onDraw(c, parent, state)

        //현재 맨 위에 있는 view를 얻는다 현재 recyclerView에 보이는 첫번째 뷰
        val topChild = parent.getChildAt(0) ?: return

        //맨 위에있는 view의 position을 얻는다
        val topChildPosition = parent.getChildAdapterPosition(topChild)

        //현재 맨 위에 있는 position을 이용해서 itemView를 얻는다(data까지 bind된)
        //현재 맨위에 있는 position이 헤더가 필요한 뷰인지 아닌지에 따라서 다른 itemView를 넘겨준다
        val currentHeader = sectionCallback.getHeaderLayoutView(parent, topChildPosition) ?: return

        //data까지 bind된 itemView를 Measure, Layout하는 과정을 거친다
        fixLayoutSize(parent, currentHeader, topChild.measuredHeight)

        //실제로 itemView를 그린다
        currentHeader.draw(c)

    }

    //Measure->뷰그룹과 뷰의 요소들의 크기를 결정, 뷰그룹의 크기가 측정되면 자식뷰들의 크기도 함께 측정
    //Layout->measure 단계에서 측정한 사이즈를 이용해서 각 뷰그룹은 자식뷰의 위치를 결정하는 새로운 Top-down traversal 과정을 진행
    //Draw->실제로 뷰를 그리는 과정

    private fun fixLayoutSize(parent: ViewGroup, view: View, height: Int) {

        //  onMeasure() -> MeasureSpec: EXACTLY 1080, MeasureSpec: AT_MOST 1823
        val widthSpec = View.MeasureSpec.makeMeasureSpec( //부모뷰가 자식에게 전달되는 레이아웃 요구사항 캡슐화.자식에게 전달하기 위한 규격 만들기
            parent.width, //리사이클러뷰의 가로 사이즈
            View.MeasureSpec.EXACTLY //자식뷰의 크기와 관계없이 부모뷰의 제약크기에 걸린다 지식뷰가 mateparent,고정dp일시
        )

        val heightSpec = View.MeasureSpec.makeMeasureSpec(
            parent.height, //리사이클러뷰의 높이
            View.MeasureSpec.AT_MOST //wrap으로 두엇기에
        )

        val childWidth: Int = ViewGroup.getChildMeasureSpec(
            widthSpec, //부모부로부터 전달받는 규격
           0, //부모 뷰로부터 자식뷰 사이 패딩
            view.layoutParams.width //View 객체에서 측정 및 배치 방법을 상위 요소에 알리는 데 사용
        )
        val childHeight: Int = ViewGroup.getChildMeasureSpec(
            heightSpec, //부모부로부터 전달받는 규격
            0,  //부모 뷰로부터 자식뷰 사이 패딩
            view.layoutParams.height //View 객체에서 측정 및 배치 방법을 상위 요소에 알리는 데 사용
        )
        view.measure(childWidth, childHeight) //헤더 그리려고 view의 measure은 호출한 단계 자식뷰들에게 제공할 수 있는 제약정보를 파라미터로 넘김
        //부모와 상대적인 왼 / 위 / 오 = 1080 / 밑 = 192  ->뷰의 위치를 배치 좌표
        view.layout(0, 0, view.measuredWidth, view.measuredHeight) //뷰의 위치를 배치하기
    }

    interface SectionCallback {
        fun getHeaderLayoutView(list: RecyclerView, position: Int): View? //position주면 View반환하기 위함
    }
}


