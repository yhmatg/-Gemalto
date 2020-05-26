package com.esimtek.esim.adapter

import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.esimtek.esim.R
import com.esimtek.esim.util.ZxingUtil
import kotlinx.android.synthetic.main.item_package_list.view.*
import java.util.HashSet
import kotlin.collections.ArrayList

class ESLListAdapter constructor(
        val list: ArrayList<String> = ArrayList(),
        val set: HashSet<String> = HashSet()
) : RecyclerView.Adapter<ESLListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_package_list, parent, false)
    )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.number.text = (position + 1).toString()
        holder.codeImageView.setImageBitmap(ZxingUtil.createOneDCode(list[position]))
        holder.codeTextView.text = list[position]
        holder.itemView.setOnLongClickListener {
            AlertDialog.Builder(it.context).setTitle(it.context.getString(R.string.alert_dialog_title))
                    .setMessage(it.context.getString(R.string.are_you_sure_to_delete))
                    .setNegativeButton(it.context.getString(R.string.cancel)) { dialog, _ -> dialog.cancel() }
                    .setPositiveButton(it.context.getString(R.string.sure)) { _, _ -> removeItem(list[position], position) }
                    .show()
            return@setOnLongClickListener true
        }
    }

    fun addItem(item: String) {
        if (set.add(item)) {
            list.add(item)
            notifyItemInserted(itemCount)
        }
    }

    fun clear() {
        set.clear()
        list.clear()
        notifyDataSetChanged()
    }

    private fun removeItem(item: String, position: Int) {
        if (set.remove(item)) {
            list.remove(item)
            notifyItemRemoved(position)
            notifyItemRangeChanged(0, itemCount)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val number: TextView = view.tv_number
        val codeImageView: ImageView = view.iv_code
        val codeTextView: TextView = view.tv_code
    }
}