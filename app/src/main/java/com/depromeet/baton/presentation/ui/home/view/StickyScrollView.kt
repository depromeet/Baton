package com.depromeet.baton.presentation.ui.home.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView


class StickyScrollView : ScrollView, ViewTreeObserver.OnGlobalLayoutListener {

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attr: AttributeSet?) : this(context, attr, 0)
    constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attr,
        defStyleAttr
    ) {
        overScrollMode = OVER_SCROLL_NEVER
        //overScrollMode를 NEVER로 설정한다. NEVER로 안하면 역스크롤시 또잉~하는 애니메이션이 만들어진다.
        viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    var header: View? = null
        set(value) {
            field = value
            field?.let {
                it.translationZ = 1f  //헤더를 설정할 때, translationZ를 1로 설정해준다. 이렇게 하지 않으면 천장에 붙은 뷰가 다른 스크롤 뷰의 레이아웃 뒤에 가려지게 된다.
                it.setOnClickListener { _ ->
                    //클릭 시, 헤더뷰가 최상단으로 오게 스크롤 이동
                    this.smoothScrollTo(scrollX, it.top)
                    callStickListener()
                    //헤더를 클릭하면 스크롤이 되면서 헤더가 천장에 찰싹 달라 붙게 구현했다.
                    //smoothScrollTo를 이용하면 지정한 스크롤의 위치로 부드럽게 스크롤
                }
            }
        }

    var stickListener: (View) -> Unit = {}
    var freeListener: (View) -> Unit = {}

    private var mIsHeaderSticky = false
    private var mHeaderInitPosition = 0f
    /* 헤더가 천장에 달라 붙어 있는지 아닌지를 체크하는 flag를 하나 선언한다.
     그리고 헤더의 초기 위치를 저장할 변수를 선언한다.
     이 변수는 스크롤되는 위치와 비교되어서 헤더가 천장을 넘어서는 스크롤인지 아닌지 판단하는데 이용된다.*/


    //레이아웃에 변경이 생길 때 일어날 것을 추가
    //헤더의 y포지션을 저장하는 부분이다.
    //만약 이 로직을 생성자나 onAttachToWindow등에 넣어두면 올바르게 저장되지 않는다
    override fun onGlobalLayout() {
        mHeaderInitPosition = header?.top?.toFloat() ?: 0f
    }

    /* ['스크롤 뷰가 스크롤 된 정도'가 '원래 있단 헤더의 y포지션' 보다 큰 경우] 라는 것은
     [헤더가 천장을 넘어섰다]*/
    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)

        val scrolly = t

        /* 따라서 헤더가 천장을 넘어섰을 때 stickHeader함수를 통해 천장에 붙이고,
         그렇지 않을 경우 freeHeader를 통해 천장에서 다시 땐다.*/
        if (scrolly > mHeaderInitPosition) {
            stickHeader(scrolly - mHeaderInitPosition)
        } else {
            freeHeader()
        }
    }

    /*    천장에 붙이는 부분이다.
        헤더 뷰의 translationY를 파라미터로 들어온 position으로 변경한다.
        translationY는 뷰의 top포지션에 대한 상대적인 포지션을 의미한다. (기본은 0이다)
        파라미터로 들어온 포지션은 scrolly - mHeaderInitPosition인데, 이것은 [헤더를 넘어서서 스크롤 된만큼]을 의마한다.
        즉, 헤더의 위치를 아래로 쭈우우우욱 늘려주는 것이다.
        그 후 리스너를 콜해준다.
        스크롤이 변화할 때마다 -> 헤더가 천장을 넘어섰는지 확인하고 -> 넘어섰으면 헤더를 천장으로 위치를 옮긴다*/
    private fun stickHeader(position: Float) {
        header?.translationY = position
        callStickListener()
    }

    // 붙어있지 않으면 -> 리스너 콜 -> flag를 true로.
    private fun callStickListener() {
        if (!mIsHeaderSticky) {
            stickListener(header ?: return)
            mIsHeaderSticky = true
        }
    }

    /*freeHeader부분이다.
    헤더의 translationY를 0으로 해서 복원*/
    private fun freeHeader() {
        header?.translationY = 0f
        callFreeListener()
    }

    private fun callFreeListener() {
        if (mIsHeaderSticky) {
            freeListener(header ?: return)
            mIsHeaderSticky = false
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewTreeObserver.removeOnGlobalLayoutListener(this)
    }

}