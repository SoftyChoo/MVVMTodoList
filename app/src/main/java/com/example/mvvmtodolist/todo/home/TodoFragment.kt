package com.example.mvvmtodolist.todo.home

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
import com.example.mvvmtodolist.databinding.TodoFragmentBinding
import com.example.mvvmtodolist.main.BookmarkState
import com.example.mvvmtodolist.main.SharedViewModel
import com.example.mvvmtodolist.main.TodoState
import com.example.mvvmtodolist.todo.content.TodoContentActivity
import com.example.mvvmtodolist.todo.content.TodoContentType
import java.util.concurrent.atomic.AtomicLong

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

    //현업코드 by viewModels -> 의존성 추가해주어야함.'
    private val viewModel: TodoViewModel by viewModels { TodoViewModelFactory(AtomicLong(1L)) }

    private val sharedViewModel: SharedViewModel by activityViewModels()

    //on -> sharedViewModel에 event를 보내고 전송

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
                    TodoContentType.EDIT -> {
                        Log.d("test", " ${todoModel?.id}")
                        modifyTodoItem(todoModel, position)
                        todoModel?.let { modifyToBookmarkTab(it) }
                    }

                    TodoContentType.REMOVE -> {
                        removeItemTodoItem(position)
                        todoModel?.let { removeToBookmarkTab(it) }
                    }

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
            onBookmarkChecked = { position, item ->
                if (item.isBookmark) {
                    addItemToBookmarkTab(item) // BookMarkTap에 Item 추가
                } else {
                    removeToBookmarkTab(item) /// BookMarkTap에서 Item 제거
                }
                modifyTodoItem(item, position) // BookMark가 check됐을 때 Item수정
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        sharedViewModel.modifyTodoItem.observe(viewLifecycleOwner, Observer { newData -> modifyTodoItem(todoModel = newData) })
        sharedViewModel.TodoState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is TodoState.ModifyTodo -> modifyTodoItem(state.todoModel)
            }
        })

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
        list.observe(viewLifecycleOwner) { // Fragment LV : observe(viewLifecycleOwner)
            listAdapter.submitList(it)
        }
    }

    private fun initView() = with(binding) {
        todoList.adapter = listAdapter
    }

    fun setDodoContent(todoModel: TodoModel?) {
        viewModel.addTodoItem(todoModel)
    }

    /** 아이템을 수정합니다.*/
    fun modifyTodoItem(
        todoModel: TodoModel?,
        position: Int? = null
    ) {
        viewModel.modifyTodoItem(
            position,
            todoModel
        )
    }

    /** 아이템을 삭제합니다.*/
    private fun removeItemTodoItem(position: Int?) {
        viewModel.removeTodoItem(position)
    }

    /** Bookmark Tab 에 아이템을 추가합니다.*/
    private fun addItemToBookmarkTab(
        item: TodoModel
    ) {
        //sharedViewModel.addBookmarkItem.value = item.toBookmarkModel()
        //->
        sharedViewModel.bookmarkState.value =
            BookmarkState.AddBookmark(item.toBookmarkModel()) // 함수

//        (activity as MainActivity).addBookmarkItem(item) //다음의 통해 진행하면 메모리 누수, 생명주기..? 등 다양한 문제가 발생할 수 있다.
        // -> 바로 MainActivity 뷰모델에 접근하는게 좋다.
        //MainViewModel.addBookmarkItem이런식
        // 형 변환을 통해 현재 호스팅이 MainActivity라는 것을 확신
    }

    /** Bookmark Tab 에 아이템을 삭제합니다.*/
    private fun removeToBookmarkTab(
        item: TodoModel
    ) {
//        sharedViewModel.removeBookmarkItem.value = item.toBookmarkModel()
        sharedViewModel.bookmarkState.value = BookmarkState.RemoveBookmark(item.toBookmarkModel())
//        (activity as MainActivity).removeBookmarkItem(item)
    }

    private fun modifyToBookmarkTab(
        item: TodoModel
    ) {
        sharedViewModel.bookmarkState.value = BookmarkState.ModifyBookmark(item.toBookmarkModel())
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}