package com.kavics.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kavics.KavicListActivity
import com.kavics.R
import com.kavics.model.Kavic
import kotlinx.android.synthetic.main.row_kavic.view.*

class SimpleItemRecyclerViewAdapter :
    RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

    private val kavicList = mutableListOf<Kavic>()

    var itemClickListener: KavicItemClickListener? = null

    //this sets the kavics
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_kavic, parent, false)
        return ViewHolder(view)
    }

    //this sets the arguments of every kavics shown in the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val kavic = kavicList[position]

        holder.kavic = kavic

        holder.tvTitle.text = kavic.title
        holder.tvDueDate.text = kavic.dueDate
    }

    //adding a new kavic to the list
    fun addItem(kavic: Kavic) {
        val size = kavicList.size
        kavicList.add(kavic)
        notifyItemInserted(size)
    }

    //adding multiple kavics to the list
    fun addAll(kavics: List<Kavic>) {
        val size = kavicList.size
        kavicList += kavics
        notifyItemRangeInserted(size, kavics.size)
    }

    //delete a kavic from the recyclerview
    fun deleteRow(position: Int) {
        kavicList.removeAt(position)
    }

    fun getKavicIdFromPosition(position: Int): Int {
        return kavicList[position].id
    }

    override fun getItemCount() = kavicList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDueDate: TextView = itemView.tvDueDate
        val tvTitle: TextView = itemView.tvTitle
        private val btnDone: Button = itemView.btnDone

        var kavic: Kavic? = null

        init {
            itemView.setOnClickListener {
                kavic?.let { kavic -> itemClickListener?.onItemClick(kavic) }
            }

            itemView.setOnLongClickListener { view ->
                itemClickListener?.onItemLongClick(adapterPosition, view)
                true
            }

            btnDone.setOnClickListener {
                kavic?.let { kavic -> KavicListActivity.kavicDAO.setDoneKavics(kavic.id) }
            }
        }
    }
}