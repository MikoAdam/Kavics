package com.kavics.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kavics.R
import com.kavics.model.DeadlineItem
import com.kavics.model.Item
import com.kavics.model.KavicItem
import kotlinx.android.synthetic.main.deadline_item.view.*
import kotlinx.android.synthetic.main.kavic_cardview.view.*


class SimpleItemRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val itemList = mutableListOf<Item>()
    var itemClickListener: KavicItemClickListener? = null
    private val dateHelper = deadlineHelper()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.kavic_cardview, parent, false)
            KavicCardViewViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.deadline_item, parent, false)
            DeadlineDateViewViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemList[position] is KavicItem) {
            1
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val kavic = itemList[position]

        if (holder is KavicCardViewViewHolder) {
            holder.kavicItem = kavic as KavicItem
            holder.tvTitle.text = kavic.title
            holder.tvDate.text = kavic.deadline
        } else if (holder is DeadlineDateViewViewHolder) {

            holder.kavicItem = kavic as DeadlineItem

            when (kavic.deadline) {
                dateHelper.getToday() -> {
                    holder.setBackgroundColor(Color.RED)
                    holder.tvDeadlineDate.text = R.string.today.toString()
                }
                dateHelper.getTomorrow() -> {
                    holder.setBackgroundColor(Color.YELLOW)
                    holder.tvDeadlineDate.text = R.string.tomorrow.toString()
                }
                else -> {
                    holder.setBackgroundColor(Color.GREEN)
                    holder.tvDeadlineDate.text = kavic.deadline
                }
            }

        }
    }

    fun addAll(kavicItems: List<KavicItem>) {
        val kavicsAndDates = dateHelper.getListWithDates(kavicItems)
        itemList.clear()
        itemList.addAll(kavicsAndDates)
        notifyDataSetChanged()
    }

    override fun getItemCount() = itemList.size

    inner class KavicCardViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTitle: TextView = itemView.textViewKavicItemTitle
        val tvDate: TextView = itemView.textViewKavicItemDate
        var kavicItem: KavicItem? = null

        init {
            itemView.setOnClickListener {
                kavicItem?.let { kavic -> itemClickListener?.onItemClick(kavic) }
            }

            itemView.setOnLongClickListener { view ->
                kavicItem?.let { itemClickListener?.onItemLongClick(adapterPosition, view, it) }
                true
            }

        }
    }

    inner class DeadlineDateViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvDeadlineDate: TextView = itemView.textViewDateName
        var kavicItem: Item? = null

        fun setBackgroundColor(color: Int) {
            itemView.setBackgroundColor(color)
        }

    }
}