package com.barran.example.mdtest

import android.app.Activity
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * RecyclerView 实现滑动删除和拖拽功能
 *
 * Created by tanwei on 2017/3/9.
 */
class SwipeDeleteActivity : Activity() {

    private var recyclerView: RecyclerView? = null

    private var adapter: Adapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe_delete)

        recyclerView = findViewById(
                R.id.activity_swipe_delete_recycler_view)
        recyclerView!!.layoutManager = LinearLayoutManager(this)

        adapter = Adapter()
        val itemTouchHelper = ItemTouchHelper(
                RecycleItemTouchHelper(adapter!!))
        itemTouchHelper.attachToRecyclerView(recyclerView)
        recyclerView!!.adapter = adapter
    }

    internal inner class RecycleItemTouchHelper(private val helperCallback: ItemTouchHelperCallback) : ItemTouchHelper.Callback() {

        private val TAG = "RecycleItemTouchHelper"

        /**
         * 设置滑动类型标记
         *
         * @param recyclerView
         * @param viewHolder
         * @return 返回一个整数类型的标识，用于判断Item那种移动行为是允许的
         */
        override fun getMovementFlags(recyclerView: RecyclerView,
                                      viewHolder: RecyclerView.ViewHolder): Int {
            Log.e(TAG, "getMovementFlags: ")
            // START 右向左 END左向右 LEFT 向左 RIGHT向右 UP向上
            // 如果某个值传0，表示不触发该操作，次数设置支持上下拖拽，支持向右滑动
            return makeMovementFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                    ItemTouchHelper.END)
        }

        /**
         * Item是否支持长按拖动
         *
         * @return true 支持长按操作 false 不支持长按操作
         */
        override fun isLongPressDragEnabled(): Boolean {
            return super.isLongPressDragEnabled()
        }

        /**
         * Item是否支持滑动
         *
         * @return true 支持滑动操作 false 不支持滑动操作
         */
        override fun isItemViewSwipeEnabled(): Boolean {
            return super.isItemViewSwipeEnabled()
        }

        /**
         * 拖拽切换Item的回调
         *
         * @param recyclerView
         * @param viewHolder
         * @param target
         * @return 如果Item切换了位置，返回true；反之，返回false
         */
        override fun onMove(recyclerView: RecyclerView,
                            viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            Log.e(TAG, "onMove: ")
            helperCallback.onMove(viewHolder.adapterPosition,
                    target.adapterPosition)
            return true
        }

        /**
         * 滑动Item
         *
         * @param viewHolder
         * @param direction
         * Item滑动的方向
         */
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            Log.e(TAG, "onSwiped: ")
            helperCallback.onItemDelete(viewHolder.adapterPosition)
        }

        /**
         * Item被选中时候回调
         *
         * @param viewHolder
         * @param actionState
         * 当前Item的状态 ItemTouchHelper.ACTION_STATE_IDLE 闲置状态
         * ItemTouchHelper.ACTION_STATE_SWIPE 滑动中状态
         * ItemTouchHelper#ACTION_STATE_DRAG 拖拽中状态
         */
        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?,
                                       actionState: Int) {
            super.onSelectedChanged(viewHolder, actionState)
        }

        override fun onChildDraw(c: Canvas, recyclerView: RecyclerView,
                                 viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int,
                                 isCurrentlyActive: Boolean) {
            // 滑动时自己实现背景及图片
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                // dX大于0时向右滑动，小于0向左滑动
                val itemView = viewHolder.itemView// 获取滑动的view
                val resources = resources
                val bitmap = BitmapFactory.decodeResource(resources,
                        R.drawable.delete)// 获取删除指示的背景图片
                val padding = 10// 图片绘制的padding
                val maxDrawWidth = 2 * padding + bitmap.width// 最大的绘制宽度
                val paint = Paint()
                paint.color = resources.getColor(R.color.btninvalid)
                val x = abs(dX).roundToInt()
                val drawWidth = min(x, maxDrawWidth)// 实际的绘制宽度，取实时滑动距离x和最大绘制距离maxDrawWidth最小值
                val itemTop = itemView.bottom - itemView.height// 绘制的top位置
                // 向右滑动
                if (dX > 0) {
                    // 根据滑动实时绘制一个背景
                    c.drawRect(itemView.left.toFloat(), itemTop.toFloat(), drawWidth.toFloat(),
                            itemView.bottom.toFloat(), paint)
                    // 在背景上面绘制图片
                    if (x > padding) {// 滑动距离大于padding时开始绘制图片
                        // 指定图片绘制的位置
                        val rect = Rect()// 画图的位置
                        rect.left = itemView.left + padding
                        rect.top = itemTop + (itemView.bottom - itemTop - bitmap.height) / 2// 图片居中
                        val maxRight = rect.left + bitmap.width
                        rect.right = min(x, maxRight)
                        rect.bottom = rect.top + bitmap.height
                        // 指定图片的绘制区域
                        var rect1: Rect? = null
                        if (x < maxRight) {
                            rect1 = Rect()// 不能再外面初始化，否则dx大于画图区域时，删除图片不显示
                            rect1.left = 0
                            rect1.top = 0
                            rect1.bottom = bitmap.height
                            rect1.right = x - padding
                        }
                        c.drawBitmap(bitmap, rect1, rect, paint)
                    }
                    val alpha = 1.0f - abs(dX) / itemView.width.toFloat()
                    itemView.alpha = alpha
                    // 绘制时需调用平移动画，否则滑动看不到反馈
                    itemView.translationX = dX
                } else {
                    // 如果在getMovementFlags指定了向左滑动（ItemTouchHelper。START）时则绘制工作可参考向右的滑动绘制，也可直接使用下面语句交友系统自己处理
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState,
                            isCurrentlyActive)
                }
            } else {
                // 拖动时有系统自己完成
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState,
                        isCurrentlyActive)
            }

        }

    }

    internal interface ItemTouchHelperCallback {
        fun onItemDelete(positon: Int)

        fun onMove(fromPosition: Int, toPosition: Int)
    }

    internal class Adapter : RecyclerView.Adapter<Holder>(), ItemTouchHelperCallback {

        private val list: MutableList<String>

        init {
            list = ArrayList()
            for (i in 0..19) {
                list.add("item$i")
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int): Holder {
            val tv = TextView(parent.context)
            tv.setPadding(0, 30, 0, 30)
            return Holder(tv)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.setContent(list[position])
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onItemDelete(position: Int) {
            list.removeAt(position)
            notifyItemRemoved(position)
        }

        override fun onMove(fromPosition: Int, toPosition: Int) {
            // 交换数据
            Collections.swap(list, fromPosition, toPosition)
            notifyItemMoved(fromPosition, toPosition)
        }
    }

    internal class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tv: TextView = itemView as TextView

        fun setContent(content: String) {
            tv.text = content
        }
    }
}
