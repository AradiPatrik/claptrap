package com.aradipatrik.claptrap.feature.todos.edit.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.aradipatrik.claptrap.feature.todos.R
import com.aradipatrik.claptrap.feature.todos.edit.model.EditTodoViewEffect
import com.aradipatrik.claptrap.feature.todos.edit.model.EditTodoViewEvent.DoneClick
import com.aradipatrik.claptrap.feature.todos.edit.model.EditTodoViewEvent.NameChanged
import com.aradipatrik.claptrap.feature.todos.edit.model.EditTodoViewModel
import com.aradipatrik.claptrap.feature.todos.edit.model.EditTodoViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_edit_todo.*
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import ru.ldralighieri.corbind.view.clicks
import ru.ldralighieri.corbind.widget.textChanges

@AndroidEntryPoint
class EditTodoFragment : Fragment(R.layout.fragment_edit_todo) {
  private val viewModel by viewModels<EditTodoViewModel>()

  private val viewEvents get() = merge(
    todo_name_text_input_layout.editText!!.textChanges()
      .drop(1)
      .map { NameChanged(newName = it.toString()) },
    done_fab.clicks()
      .map { DoneClick }
  )

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewModel.setArguments(arguments)
    viewModel.viewState
      .onEach(::render)
      .launchIn(lifecycleScope)

    viewEvents
      .onEach(viewModel::processInput)
      .launchIn(lifecycleScope)

    viewModel.viewEffects.receiveAsFlow()
      .onEach(::handleViewEffects)
      .launchIn(lifecycleScope)
  }

  private fun render(viewState: EditTodoViewState) = when(viewState) {
    EditTodoViewState.Loading -> edit_todo_view_flipper.displayedChild = 0
    is EditTodoViewState.Editing -> {
      edit_todo_view_flipper.displayedChild = 1
      with(todo_name_text_input_layout.editText!!) {
        if (text.toString() != viewState.todo.name) {
          setText(viewState.todo.name)
        }
      }
    }
    is EditTodoViewState.Saving -> edit_todo_view_flipper.displayedChild = 0
  }

  private fun handleViewEffects(viewEffect: EditTodoViewEffect) = when(viewEffect) {
    EditTodoViewEffect.NavigateBack -> findNavController().popBackStack()
  }
}
