package com.davila.alessandro.laboratoriocalificado03

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TeacherAdapter(
    private var list: List<Teacher>,
    private val onItemClick: (Teacher) -> Unit,
    private val onItemLongClick: (Teacher) -> Unit
) : RecyclerView.Adapter<TeacherAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.iv_avatar)
        val name: TextView = view.findViewById(R.id.tv_name)
        val lastName: TextView = view.findViewById(R.id.tv_lastname)
        val email: TextView = view.findViewById(R.id.tv_email)
        val phone: TextView = view.findViewById(R.id.tv_phone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_teacher, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val teacher = list[position]
        holder.name.text = teacher.name
        holder.lastName.text = teacher.last_name
        holder.email.text = teacher.email
        holder.phone.text = teacher.phone

        Glide.with(holder.itemView.context)
            .load(teacher.imageUrl)
            .placeholder(R.drawable.ic_person_placeholder)
            .error(R.drawable.ic_person_error)
            .circleCrop()
            .into(holder.img)

        holder.itemView.setOnClickListener { onItemClick(teacher) }
        holder.itemView.setOnLongClickListener {
            onItemLongClick(teacher)
            true
        }
    }

    override fun getItemCount() = list.size

    fun updateList(newList: List<Teacher>) {
        list = newList
        notifyDataSetChanged()
    }
}
