package com.aradipatrik.claptrap.feature.todos.edit.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aradipatrik.claptrap.feature.todos.R
import com.aradipatrik.claptrap.feature.todos.edit.model.EditTodoViewEffect
import com.aradipatrik.claptrap.feature.todos.edit.model.EditTodoViewEvent
import com.aradipatrik.claptrap.feature.todos.edit.model.EditTodoViewEvent.DoneClick
import com.aradipatrik.claptrap.feature.todos.edit.model.EditTodoViewEvent.NameChanged
import com.aradipatrik.claptrap.feature.todos.edit.model.EditTodoViewModel
import com.aradipatrik.claptrap.feature.todos.edit.model.EditTodoViewState
import com.aradipatrik.claptrap.mvi.ClapTrapFragment
import com.aradipatrik.claptrap.mvi.MviUtil.ignore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_edit_todo.*
import kotlinx.coroutines.flow.*
import ru.ldralighieri.corbind.view.clicks
import ru.ldralighieri.corbind.widget.textChanges

@AndroidEntryPoint
class EditTodoFragment : ClapTrapFragment<EditTodoViewState, EditTodoViewEvent, EditTodoViewEffect>(
  R.layout.fragment_edit_todo
) {
  override val viewModel by viewModels<EditTodoViewModel>()

  override val viewEvents
    get() = merge(
      todo_name_text_input_layout.editText!!.textChanges()
        .drop(1)
        .map { NameChanged(newName = it.toString()) },
      done_fab.clicks()
        .map { DoneClick }
    )

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewModel.setArguments(arguments)
  }

  override fun render(viewState: EditTodoViewState) = when (viewState) {
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

  override fun react(viewEffect: EditTodoViewEffect) = when (viewEffect) {
    EditTodoViewEffect.NavigateBack -> findNavController().popBackStack().ignore()
  }
}
