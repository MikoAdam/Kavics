package com.kavics.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kavics.R
import com.kavics.model.DeadlineItem
import com.kavics.model.Item
import com.kavics.model.OneTimeKavicItem
import kotlinx.android.synthetic.main.deadline_item.view.*
import kotlinx.android.synthetic.main.kavic_cardview.view.*
import java.util.*
import kotlin.concurrent.schedule

class SimpleItemRecyclerViewAdapter(val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var timer: TimerTask
    private val kavicList = mutableListOf<Item>()
    var itemClickListener: KavicItemClickListener? = null
    private val dateHelper = DateHelper()

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
        return if (kavicList[position] is OneTimeKavicItem) {
            1
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val kavic = kavicList[position]

        if (holder is KavicCardViewViewHolder) {

            holder.oneTimeKavicItem = kavic as OneTimeKavicItem
            holder.tvTitle.text = kavic.title
            holder.checkBoxDone.isChecked = false
            val howManyMinutes = "${kavic.howManyMinutes} mins"
            holder.tvHowManyMinutes.text = howManyMinutes

        } else if (holder is DeadlineDateViewViewHolder) {

            holder.kavicItem = kavic as DeadlineItem
            val displayHowManyMinutes = "${kavic.howManyMinutes} mins"
            holder.textViewHowManyMinutesSum.text = displayHowManyMinutes

            when (kavic.deadline) {
                dateHelper.getToday() -> {
                    holder.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.deadlineToday
                        )
                    )
                    holder.tvDeadlineDate.text = context.getString(R.string.today)
                }
                dateHelper.getTomorrow() -> {
                    holder.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.deadlineTomorrow
                        )
                    )
                    holder.tvDeadlineDate.text = context.getString(R.string.tomorrow)
                }
                else -> {
                    holder.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.deadlineLater
                        )
                    )
                    holder.tvDeadlineDate.text = kavic.deadline
                }
            }
        }
    }

    fun addAll(oneTimeKavicItems: List<OneTimeKavicItem>) {
        val kavicsAndDates = dateHelper.getKavicListWithDeadlineItems(oneTimeKavicItems)
        kavicList.clear()
        kavicList.addAll(kavicsAndDates)
        notifyDataSetChanged()
    }

    override fun getItemCount() = kavicList.size

    inner class KavicCardViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTitle: TextView = itemView.textViewKavicItemTitle
        var oneTimeKavicItem: OneTimeKavicItem? = null
        val checkBoxDone: CheckBox = itemView.checkBoxDone
        val tvHowManyMinutes: TextView = itemView.textViewHowManyMinutes

        init {
            itemView.setOnClickListener {
                oneTimeKavicItem?.let { kavic -> itemClickListener?.onItemClick(kavic) }
            }

            itemView.setOnLongClickListener { view ->
                oneTimeKavicItem?.let {
                    itemClickListener?.onItemLongClick(
                        adapterPosition,
                        view,
                        it
                    )
                }
                true
            }

            checkBoxDone.setOnClickListener {
                timer = Timer("Delay before kavic disappears", false).schedule(500) {
                    oneTimeKavicItem?.let { kavic -> itemClickListener?.checkBoxCheckedPopUp(kavic) }



                    timer.cancel()
                }
            }

        }
    }

    inner class DeadlineDateViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvDeadlineDate: TextView = itemView.textViewDeadlineDate
        var kavicItem: Item? = null
        val textViewHowManyMinutesSum: TextView = itemView.textViewHowManyMinutesSum

        fun setBackgroundColor(color: Int) {
            itemView.setBackgroundColor(color)
        }

    }

}