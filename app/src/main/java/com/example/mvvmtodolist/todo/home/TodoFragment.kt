package com.example.mvvmtodolist.todo.home

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mvvmtodolist.databinding.TodoFragmentBinding
import com.example.mvvmtodolist.todo.content.TodoContentActivity
import com.example.mvvmtodolist.todo.content.TodoContentType

class TodoFragment : Fragment() {

    companion object {
        fun newInstance() = TodoFragment()
    }

    private var _binding: TodoFragmentBinding? = null
    private val binding get() = _binding!!

    //AAC viewModel을 선언할 때 다른얘들과는 다르게 provider를 통해 선언해주어야한다.
//    private val viewModel : TodoViewModel by lazy {
//        ViewModelProvider(this).get(TodoViewModel::class.java)
//    }

    //현업코드 by viewModels -> 의존성 추가해주어야함.
    private val viewModel : TodoViewModel by viewModels()


    private val editTodoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val entryType =
                    result.data?.getStringExtra(TodoContentActivity.EXTRA_TODO_ENTRY_TYPE)
                val position = result.data?.getIntExtra(TodoContentActivity.EXTRA_TODO_POSITION, -1)
                val todoModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
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
                    TodoContentType.EDIT -> modifyTodoItem(position, todoModel)
                    TodoContentType.REMOVE -> removeItemTodoItem(position)
                    else -> Unit // nothing
                }
            }
        }

    private val listAdapter by lazy {
        TodoListAdapter { position, item ->
            editTodoLauncher.launch(
                TodoContentActivity.newIntentForEdit(
                    requireContext(),
                    position,
                    item
                )
            )
        }
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
        initModel() // viewModel
    }

    private fun initModel() = with(viewModel) { // viewModel
        // viewModel 상 읽기용 list
        list.observe(viewLifecycleOwner){ // Fragment LV : observe(viewLifecycleOwner)
            listAdapter.submitList(it)
        }
    }

    private fun initView() = with(binding) {
        todoList.adapter = listAdapter
    }

    fun setDodoContent(todoModel: TodoModel?) {
        viewModel.addTodoItem(todoModel)
    }

    /**
     * 아이템을 수정합니다.
     */
    private fun modifyTodoItem(
        position: Int?,
        todoModel: TodoModel?
    ) {
        viewModel.modifyItem(
            position,
            todoModel
        )
    }

    /**
     * 아이템을 삭제합니다.
     */
    private fun removeItemTodoItem(position: Int?) {
        viewModel.removeItem(position)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}