package com.example.taskapp.Adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.example.taskapp.AddNewTask
import com.example.taskapp.MainActivity
import com.example.taskapp.Model.ToDoModel
import com.example.taskapp.R
import com.example.taskapp.Utils.DatabaseHandler

class ToDoAdapter(private val db: DatabaseHandler, private val activity: MainActivity) :
    RecyclerView.Adapter<ToDoAdapter.ViewHolder>() {

    private var todoList: List<ToDoModel>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        db.openDatabase()
        val item = todoList!![position]
        holder.task.text = item.task
        holder.task.isChecked = toBoolean(item.status)
        holder.task.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                db.updateStatus(item.id, 1)
            } else {
                db.updateStatus(item.id, 0)
            }
        }
    }

    override fun getItemCount(): Int {
        return todoList?.size ?: 0
    }

    private fun toBoolean(n: Int): Boolean {
        return n != 0
    }

    fun setTasks(todoList: List<ToDoModel>) {
        this.todoList = todoList
        notifyDataSetChanged()
    }

    fun editItem(position: Int) {
        val item = todoList?.get(position)
        item?.let {
            val bundle = Bundle().apply {
                putInt("id", it.id)
                putString("task", it.task)
            }
            val fragment = AddNewTask()
            fragment.arguments = bundle
            fragment.show(activity.supportFragmentManager, AddNewTask.TAG)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var task: CheckBox = view.findViewById(R.id.todoCheckBox)
    }
}