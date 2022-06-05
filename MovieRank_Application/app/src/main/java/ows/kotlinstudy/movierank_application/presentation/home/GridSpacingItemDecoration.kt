package ows.kotlinstudy.movierank_application.presentation.home

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * spanCount에 맞게 spacing 조정해주는 ItemDecoration 클래스
 */
class GridSpacingItemDecoration(
    private val spanCount: Int,
    private val spacing: Int
) : RecyclerView.ItemDecoration() {

    /**
     * outRect를 활용해서 값들을 대입하면 그만큼 여백이 생
     */
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val adapterPosition = parent.getChildAdapterPosition(view)
        val gridLayoutManager = parent.layoutManager as GridLayoutManager
        val spanSize = gridLayoutManager.spanSizeLookup.getSpanSize(adapterPosition)

        /**
         * spanCount만큼 모두 차지할 경우 상하좌우 여백 동일하게 spacing 만큼 할당
         */
        if (spanSize == spanCount) {
            outRect.left = spacing
            outRect.right = spacing
            outRect.top = spacing
            outRect.bottom = spacing
            return
        }

        /**
         * spanSizeLoopup으로 spanCount가 일정하기 않기 때문에 adapterposition 사용하기 어려움 -> 일정하지 않는 패턴
         * GridLayoutmanager.LayoutParams의 spanIndex 사용
         * ex)   0
         *     0,1,2
         *     0,1,2
         *
         * ex) spanCount = 3, spacing = 30
         * itemHorizontalSpacing = 40
         *
         */
        val column = (view.layoutParams as GridLayoutManager.LayoutParams).spanIndex
        val itemHorizontalSpacing = ((spanCount + 1) * spacing) / spanCount.toFloat()
        when (column) {
            0 -> {
                outRect.left = spacing
                outRect.right = (itemHorizontalSpacing - spacing).toInt()
            }
            (spanCount - 1) -> {
                outRect.left = (itemHorizontalSpacing - spacing).toInt()
                outRect.right = spacing
            }
            else -> {
                outRect.left = (itemHorizontalSpacing / 2).toInt()
                outRect.right = (itemHorizontalSpacing / 2).toInt()
            }
        }
        outRect.top = spacing
        outRect.bottom = spacing
    }
}