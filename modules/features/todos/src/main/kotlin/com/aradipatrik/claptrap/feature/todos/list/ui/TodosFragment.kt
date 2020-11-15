package com.aradipatrik.claptrap.feature.todos.list.ui


import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aradipatrik.claptrap.feature.todos.R
import com.aradipatrik.claptrap.feature.todos.list.model.TodoListViewEffect
import com.aradipatrik.claptrap.feature.todos.list.model.TodoListViewEffect.NavigateToEdit
import com.aradipatrik.claptrap.feature.todos.list.model.TodoListViewEvent
import com.aradipatrik.claptrap.feature.todos.list.model.TodoListViewModel
import com.aradipatrik.claptrap.feature.todos.list.model.TodoListViewState
import com.aradipatrik.claptrap.mvi.ClapTrapFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_todo_list.*

@AndroidEntryPoint
class TodosFragment : ClapTrapFragment<TodoListViewState, TodoListViewEvent, TodoListViewEffect>(
  R.layout.fragment_todo_list
) {
  override val viewModel by viewModels<TodoListViewModel>()

  private val todosAdapter = TodosAdapter(lifecycleScope)

  override val viewEvents get() = todosAdapter.viewEvents

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    todo_recycler_view.adapter = todosAdapter
    todo_recycler_view.layoutManager = LinearLayoutManager(context)
  }

  override fun render(viewState: TodoListViewState) {
    when (viewState) {
      TodoListViewState.Loading -> todo_list_view_flipper.displayedChild = 0
      is TodoListViewState.ListLoaded -> {
        todosAdapter.submitList(viewState.list)
        todo_list_view_flipper.displayedChild = 1
      }
      TodoListViewState.Error -> todo_list_view_flipper.displayedChild = 2
    }
  }

  override fun react(viewEffect: TodoListViewEffect) = when (viewEffect) {
    is NavigateToEdit -> findNavController().navigate(
      R.id.nav_action_todos_to_edit_todos,
      bundleOf("todoId" to viewEffect.todo.id)
    )
  }
}
