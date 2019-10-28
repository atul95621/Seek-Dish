package com.dish.seekdish.custom

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Pagination class to add more items to the list when reach the last item.
 */
abstract class PaginationScrollListener(internal var layoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {

    /*public abstract int getTotalPageCount();*/

    abstract fun isLastPage(): Boolean

    abstract fun isLoading(): Boolean


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        Log.e("coor", "" + "dx   " + dx + "dy   " + dy)


        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        val pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();


        if (!isLoading() && !isLastPage()) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                loadMoreItems()
            }
        }

    }


    protected abstract fun loadMoreItems()
}