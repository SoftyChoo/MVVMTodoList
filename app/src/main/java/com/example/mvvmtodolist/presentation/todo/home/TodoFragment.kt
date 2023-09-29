package com.example.mvvmtodolist.presentation.todo.home

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmtodolist.databinding.TodoFragmentBinding
import com.example.mvvmtodolist.presentation.main.MainSharedEventForTodo
import com.example.mvvmtodolist.presentation.main.MainSharedViewModel
import com.example.mvvmtodolist.presentation.todo.content.TodoContentActivity
import com.example.mvvmtodolist.presentation.todo.content.TodoContentType
import java.util.concurrent.atomic.AtomicLong

class TodoFragment : Fragment() {

    companion object {
        fun newInstance() = TodoFragment()
    }

    private var _binding: TodoFragmentBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: MainSharedViewModel by lazy {
        ViewModelProvider(requireActivity())[MainSharedViewModel::class.java]
    }

    private val viewModel: TodoViewModel by lazy {
        ViewModelProvider(
            this,
            TodoViewModelFactory()
        )[TodoViewModel::class.java]
    }

    private val editTodoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val entryType =
                    result.data?.getStringExtra(TodoContentActivity.EXTRA_TODO_ENTRY_TYPE)
                val position = result.data?.getIntExtra(TodoContentActivity.EXTRA_TODO_POSITION, -1)
                val item = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.data?.getParcelableExtra(
                        TodoContentActivity.EXTRA_TODO_MODEL,
                        TodoModel::class.java
                    )
                } else {
                    result.data?.getParcelableExtra(
                        TodoContentActivity.EXTRA_TODO_MODEL
                    )
                }

                // entry type 에 따라 기능 분리
                when (TodoContentType.from(entryType)) {
                    TodoContentType.EDIT -> viewModel.modifyTodoItem(item)
                    TodoContentType.REMOVE -> removeItemTodoItem(position)
                    else -> Unit // nothing
                }
            }
        }

    private val listAdapter by lazy {
        TodoListAdapter(
            onClickItem = { position, item ->
                editTodoLauncher.launch(
                    TodoContentActivity.newIntentForEdit(
                        requireContext(),
                        position,
                        item
                    )
                )
            },
            onBookmarkChecked = { _, item ->
                viewModel.modifyTodoItem(
                    item = item
                )
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TodoFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {
        todoList.adapter = listAdapter
    }

    private fun initViewModel() {
        with(viewModel) {
            list.observe(viewLifecycleOwner) {
                listAdapter.submitList(it)
                sharedViewModel.updateBookmarkItems(it)
            }
        }

        with(sharedViewModel) {
            todoEvent.observe(viewLifecycleOwner) { event ->
                when (event) {
                    is MainSharedEventForTodo.UpdateTodoItem -> {
                        viewModel.modifyTodoItem(event.item)
                    }
                }
            }
        }
    }


    fun setDodoContent(
        todoModel: TodoModel?
    ) {
        viewModel.addTodoItem(todoModel)
    }

    /**
     * 아이템을 삭제합니다.
     */
    private fun removeItemTodoItem(
        position: Int?
    ) {
        viewModel.removeTodoItem(position)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}