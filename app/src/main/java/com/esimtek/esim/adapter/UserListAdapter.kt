package com.esimtek.esim.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.esimtek.esim.R
import com.esimtek.esim.model.UserBean
import kotlinx.android.synthetic.main.item_user_list.view.*

class UserListAdapter constructor(
        val list: ArrayList<UserBean.DataBean.UserInfoBean> = ArrayList()
) : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {

    lateinit var longClickBean: UserBean.DataBean.UserInfoBean

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user_list, parent, false)
    )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.number.text = (position + 1).toString()
        holder.name.text = list[position].userName
        holder.time.text = list[position].setupTime.replace("T", " ").take(19)
        holder.itemView.setOnLongClickListener {
            longClickBean = list[position]
            return@setOnLongClickListener false
        }
    }

    fun addItems(items: List<UserBean.DataBean.UserInfoBean>) {
        list.clear()
        list.addAll(items)
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val number: TextView = view.tv_number
        val name: TextView = view.tv_name
        val time: TextView = view.tv_time
    }
}