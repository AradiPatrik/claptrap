package com.aradipatrik.yamm.feature.todos.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aradipatrik.yamm.domain.Todo
import com.aradipatrik.yamm.feature.todos.R
import kotlinx.android.synthetic.main.list_item_todo.view.*
import javax.inject.Inject

object TodoComparator : DiffUtil.ItemCallback<Todo>() {
  override fun areItemsTheSame(oldItem: Todo, newItem: Todo) = oldItem.id == newItem.id

  override fun areContentsTheSame(oldItem: Todo, newItem: Todo) = oldItem == newItem
}

class TodosAdapter @Inject constructor() :
  ListAdapter<Todo, TodosAdapter.ViewHolder>(TodoComparator) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
    ViewHolder(
      LayoutInflater.from(parent.context).inflate(
        R.layout.list_item_todo,
        parent,
        false
      )
    )

  override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(todo: Todo) {
      itemView.todo_text.text = todo.name
      itemView.todo_check_box.isChecked = todo.isDone
    }
  }
}
