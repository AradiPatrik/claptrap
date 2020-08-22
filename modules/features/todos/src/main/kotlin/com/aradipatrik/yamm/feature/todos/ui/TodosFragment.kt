package com.aradipatrik.yamm.feature.todos.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.aradipatrik.yamm.feature.todos.R
import com.aradipatrik.yamm.feature.todos.model.TodosViewModel
import com.aradipatrik.yamm.feature.todos.model.TodosViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_todo_list.*
import javax.inject.Inject

@AndroidEntryPoint
class TodosFragment : Fragment(R.layout.fragment_todo_list) {
  private val viewModel by viewModels<TodosViewModel>()

  @Inject
  lateinit var todosAdapter: TodosAdapter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    todo_recycler_view.adapter = todosAdapter
    todo_recycler_view.layoutManager = LinearLayoutManager(context)
    viewModel.todosViewState.observe(viewLifecycleOwner, ::render)
  }

  private fun render(viewState: TodosViewState) {
    when (viewState) {
      TodosViewState.Loading -> todo_list_view_flipper.displayedChild = 0
      is TodosViewState.ListLoaded -> {
        todosAdapter.submitList(viewState.list)
        todo_list_view_flipper.displayedChild = 1
      }
      TodosViewState.Error -> todo_list_view_flipper.displayedChild = 2
    }
  }
}
