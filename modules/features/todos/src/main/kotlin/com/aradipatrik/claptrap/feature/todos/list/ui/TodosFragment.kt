package com.aradipatrik.claptrap.feature.todos.list.ui


import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aradipatrik.claptrap.feature.todos.R
import com.aradipatrik.claptrap.feature.todos.list.model.TodoListViewEffect
import com.aradipatrik.claptrap.feature.todos.list.model.TodoListViewEffect.NavigateToEdit
import com.aradipatrik.claptrap.feature.todos.list.model.TodoListViewModel
import com.aradipatrik.claptrap.feature.todos.list.model.TodoListViewState
import com.aradipatrik.claptrap.interactors.interfaces.todo.TodoInteractor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_todo_list.*
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class TodosFragment : Fragment(R.layout.fragment_todo_list) {
  private val viewModel by viewModels<TodoListViewModel>()

  private val todosAdapter = TodosAdapter(lifecycleScope)

  private val viewEvents get() = todosAdapter.viewEvents

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    todo_recycler_view.adapter = todosAdapter
    todo_recycler_view.layoutManager = LinearLayoutManager(context)

    viewModel.viewState
      .onEach(::render)
      .launchWhenResumed()

    viewEvents.onEach(viewModel::processInput)
      .launchWhenResumed()

    viewModel.viewEffects.receiveAsFlow()
      .onEach(::handleEffect)
      .launchWhenResumed()
  }

  private fun render(viewState: TodoListViewState) {
    when (viewState) {
      TodoListViewState.Loading -> todo_list_view_flipper.displayedChild = 0
      is TodoListViewState.ListLoaded -> {
        todosAdapter.submitList(viewState.list)
        todo_list_view_flipper.displayedChild = 1
      }
      TodoListViewState.Error -> todo_list_view_flipper.displayedChild = 2
    }
  }

  private fun handleEffect(viewEffect: TodoListViewEffect) = when (viewEffect) {
    is NavigateToEdit -> findNavController().navigate(
      R.id.nav_action_todos_to_edit_todos,
      bundleOf("todoId" to viewEffect.todo.id)
    )
  }

  private fun Flow<*>.launchWhenResumed() {
    lifecycleScope.launchWhenResumed {
      this@launchWhenResumed.collect()
    }
  }
}
