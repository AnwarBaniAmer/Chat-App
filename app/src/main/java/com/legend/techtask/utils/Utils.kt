package com.legend.techtask.utils

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.legend.techtask.adapter.MessageAdapter

object Utils {

    fun isConnected(context: Context?): Boolean {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }


    fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(
            Activity.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        if (inputMethodManager.isAcceptingText) {
            inputMethodManager.hideSoftInputFromWindow(
                activity.currentFocus!!.windowToken,
                0
            )
        }
    }

    class RecyclerItemTouchHelper(
        dragDirs: Int,
        swipeDirs: Int,
        private val listener: RecyclerItemTouchHelperListener
    ) :
        ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            if (viewHolder != null) {
                getDefaultUIUtil().onSelected(getViewHolderType(viewHolder))
            }
        }

        override fun onChildDrawOver(
            c: Canvas, recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
            actionState: Int, isCurrentlyActive: Boolean
        ) {
            getDefaultUIUtil().onDrawOver(
                c, recyclerView, getViewHolderType(viewHolder), dX, dY,
                actionState, isCurrentlyActive
            )
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            getDefaultUIUtil().clearView(getViewHolderType(viewHolder))
        }

        override fun onChildDraw(
            c: Canvas, recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
            actionState: Int, isCurrentlyActive: Boolean
        ) {
            getDefaultUIUtil().onDraw(
                c, recyclerView, getViewHolderType(viewHolder), dX, dY,
                actionState, isCurrentlyActive
            )
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            listener.onSwiped(viewHolder, direction, viewHolder.bindingAdapterPosition)
        }

        interface RecyclerItemTouchHelperListener {
            fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int, position: Int)
        }

        private fun getViewHolderType(viewHolder: RecyclerView.ViewHolder?): View? {
            if (viewHolder != null) {
                if (viewHolder is MessageAdapter.SentMessageHolder)
                    return viewHolder.sentMessageBinding.viewForeground
                else if ((viewHolder is MessageAdapter.ReceivedMessageHolder))
                    return viewHolder.binding.viewForeground
            }
            return null
        }
    }
}
