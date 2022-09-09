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


/*    recyclerView가 그려진 뒤에 호출 된다.
    그래서 ReyclerView 위에 그릴 수 있다.
    이 점을 이용해서 우리는 상단에 Sticky Header View를 그릴 것이다.*/

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        Log.e("ㅡ-ㅡ", "=======================================")
        super.onDrawOver(c, parent, state)

        //현재 맨 위에 있는 view를 얻는다 현재 recyclerView에 보이는 뷰의 0번째
        val topChild = parent.getChildAt(0) ?: return //0번쨰 인덱스의 ItemView를 얻는다
        Log.e("ㅡ-ㅡ", "현재 헤더 View: $topChild") //맨 위에 있는 뷰

        //맨 위에있는 view의 position을 얻는다
        val topChildPosition = parent.getChildAdapterPosition(topChild) //ItemView의 position을 얻는다
        Log.e("ㅡ-ㅡ", "현재 맨위 포지션: $topChildPosition") //맨 위에 있는 뷰의 position이니까 0

        //현재 맨 위에 있는 position을 이용해서 itemView를 얻는다(data까지 bind된)
        val currentHeader = sectionCallback.getHeaderLayoutView(parent, topChildPosition) ?: return//헤더뷰를 얻는다
        Log.e("ㅡ-ㅡ", "현재 헤더 ItemView: $currentHeader")

        //data까지 bind된 itemView의 뷰 사이즈를 측정한다
        fixLayoutSize(parent, currentHeader, topChild.measuredHeight)

        //현재 헤더뷰의 바닥부분이 접촉포인트->int타입
        val contactPoint = currentHeader.bottom
        Log.e("ㅡ-ㅡ", "현재 해더 bottom : $contactPoint")

        //현재 헤더뷰의 bottom값을 이용해서 다음 view를 얻는다
        val childInContact: View = getChildInContact(parent, contactPoint) ?: return
        Log.e("ㅡ-ㅡ", "다음 View: $childInContact")

        //다음 view를 이용해서 다음 view의 포지션을 얻는다
        val childAdapterPosition = parent.getChildAdapterPosition(childInContact)
        Log.e("ㅡ-ㅡ", "다음 View 포지션: $childAdapterPosition")

        if (childAdapterPosition == -1) return //불필요할 것 같긴합


        //다음 뷰가 헤더가 아니면 현재 뷰를 위에 그린다
        if( !sectionCallback.isHeader(childAdapterPosition) ){
        //    drawHeader(c, currentHeader)
        }

     when {
            //다음뷰가 헤더면 현재 뷰는 밀려나는 것처럼 그린다
            sectionCallback.isHeader(childAdapterPosition) -> {
                moveHeader(c, currentHeader, childInContact)
                Log.e("ㅡ-ㅡ", "다음 포지션이 헤더?: 응")
            }
            //헤더가 아니면 현재 뷰를 고정되어있는 것처럼 보이도록 그린다
            else -> {
                drawHeader(c, currentHeader)
                Log.e("ㅡ-ㅡ", "다음 포지션이 헤더?: 아니")
            }
        }
    }


    private fun moveHeader(c: Canvas, currentHeader: View, nextHeader: View) {
        c.save() //캔퍼스를 사용하기 전(변형을 하기전:회전, 원점 이동 등....)환경을 save함수를 이용해서 저장
        //헤더가될 다음 nextHeader를 header위치로 이동시킨다
        c.translate(0f, nextHeader.top - currentHeader.height.toFloat())
        currentHeader.draw(c)
        c.restore() //캔퍼스를 변경해서 그린 후 restore()함수를 통해 캔퍼스를 사용하기 전 환경
    }

    private fun drawHeader(c: Canvas, header: View) {
        c.save()
        c.translate(0f, 0f)
        header.draw(c)
        c.restore()
    }



    //현재 뷰의 다음 뷰를 얻는다
    private fun getChildInContact(parent: RecyclerView, contactPoint: Int): View? {
        var childInContact: View? = null
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            if (child.bottom > contactPoint) {
                if (child.top <= contactPoint) {
                    childInContact = child
                    break
                }
            }
        }
        return childInContact
    }

    //todo
    //헤더 뷰를 측정하여 크기가 0보다 크고 그려질지 확인합니다.
    //Measure->뷰그룹과 뷰의 요소들의 크기를 결정, 뷰그룹의 크기가 측정되면 자식뷰들의 크기도 함께 측정
    //Layout->measure 단계에서 측정한 사이즈를 이용해서 각 뷰그룹은 자식뷰의 위치를 결정하는 새로운 Top-down traversal 과정을 진행
    //Draw->실제로 뷰를 그리는 과정

///        fixLayoutSize(parent, currentHeader, topChild.measuredHeight)
    private fun fixLayoutSize(parent: ViewGroup, view: View, height: Int) {



  /*  onMeasure() -> MeasureSpec: EXACTLY 1080, MeasureSpec: AT_MOST 1823

    match_parent는 EXACTLY 모드로 지정된 크기를 전달 받습니다.
    부모의 최대 가로 크기는 1080이며 부모와 동일한 크기를 원하는 match_parent의 속성대로 정확히 지정된 크기를 전달됩니다.*/
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
            100,
            view.layoutParams.height //View 객체에서 측정 및 배치 방법을 상위 요소에 알리는 데 사용
        )
        view.measure(childWidth, childHeight) //헤더 그리려고 view의 measure은 호출한 단계 자식뷰들에게 제공할 수 있는 제약정보를 파라미터로 넘김
        //부모와 상대적인 왼 / 위 / 오 = 1080 / 밑 = 192  ->뷰의 위치를 배치 좌표
        view.layout(0, 0, view.measuredWidth, view.measuredHeight) //뷰의 위치를 배치하기
    Log.e("ㅡ-ㅡ", "------------drawHeader"+view.measuredHeight.toString())
    }

    interface SectionCallback {
        fun isHeader(position: Int): Boolean //해당 포지션이 헤더가될 뷰인지 판단
        fun getHeaderLayoutView(list: RecyclerView, position: Int): View? //position주면 View반환
       fun getFragmentManager():FragmentManager
    }
}


