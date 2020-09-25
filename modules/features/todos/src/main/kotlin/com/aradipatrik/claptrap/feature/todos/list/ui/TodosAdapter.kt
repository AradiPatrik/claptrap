package com.aradipatrik.claptrap.feature.todos.list.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aradipatrik.claptrap.domain.Todo
import com.aradipatrik.claptrap.feature.todos.R
import com.aradipatrik.claptrap.feature.todos.list.model.TodoListViewEvent
import kotlinx.android.synthetic.main.list_item_todo.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import ru.ldralighieri.corbind.view.clicks
import ru.ldralighieri.corbind.widget.checkedChanges
import javax.inject.Inject

object TodoComparator : DiffUtil.ItemCallback<Todo>() {
  override fun areItemsTheSame(oldItem: Todo, newItem: Todo) = oldItem.id == newItem.id

  override fun areContentsTheSame(oldItem: Todo, newItem: Todo) = oldItem == newItem
}

class TodosAdapter @Inject constructor(
  private val eventScope: CoroutineScope
) :
  ListAdapter<Todo, TodosAdapter.ViewHolder>(TodoComparator) {
  private val viewEventChannel = Channel<TodoListViewEvent>(BUFFERED)
  val viewEvents = viewEventChannel.receiveAsFlow()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
    ViewHolder(
      LayoutInflater.from(parent.context).inflate(
        R.layout.list_item_todo,
        parent,
        false
      )
    ).apply {
      todoCheckedEvents
        .onEach(viewEventChannel::send)
        .launchIn(eventScope)
    }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    lateinit var todo: Todo
    val todoCheckedEvents = merge(
      itemView.todo_check_box.checkedChanges()
        .drop(1)
        .filter { todo.isDone != it }
        .map { TodoListViewEvent.TodoChecked(todo, it) },
      itemView.clicks()
        .map { TodoListViewEvent.TodoClicked(todo) }
    )

    fun bind(todo: Todo) {
      this.todo = todo
      itemView.todo_text.text = todo.name
      itemView.todo_check_box.isChecked = todo.isDone
    }
  }
}
