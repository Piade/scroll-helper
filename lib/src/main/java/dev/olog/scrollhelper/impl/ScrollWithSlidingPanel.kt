package dev.olog.scrollhelper.impl

import android.view.View
import android.view.ViewGroup
import androidx.core.math.MathUtils.clamp
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import dev.olog.scrollhelper.Input

internal class ScrollWithSlidingPanel(
    input: Input.OnlySlidingPanel
) : AbsScroll(input) {

    private val scrollSlidingPanel = input.scrollableSlidingPanel

    private val slidingPanel = input.slidingPanel.first
    private val slidingPanelHeight: Int = input.slidingPanel.second.value

    private var currentPeekHeight = slidingPanelHeight

    override fun onAttach(activity: FragmentActivity) {

    }

    override fun onDetach(activity: FragmentActivity) {

    }

    override fun restoreInitialPosition(recyclerView: RecyclerView) {
        super.restoreInitialPosition(recyclerView)
        if (!scrollSlidingPanel){
            return
        }
        currentPeekHeight = slidingPanelHeight
        slidingPanel.peekHeight = slidingPanelHeight

    }

    override fun onRecyclerViewScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onRecyclerViewScrolled(recyclerView, dx, dy)
        if (!scrollSlidingPanel){
            return
        }
        currentPeekHeight = clamp(
            currentPeekHeight - dy,
            0,
            slidingPanelHeight
        )
        slidingPanel.peekHeight = currentPeekHeight
        fabMap.get(recyclerView.hashCode())?.let { it.translationY =
            (slidingPanelHeight - currentPeekHeight).toFloat()
        }
    }

    override fun applyMarginToFab(fab: View) {
        val params = fab.layoutParams
        val marginsToApply = slidingPanelHeight

        if (params is ViewGroup.MarginLayoutParams && params.bottomMargin < marginsToApply) {
            params.bottomMargin += marginsToApply
            fab.layoutParams = params
        }
    }
}