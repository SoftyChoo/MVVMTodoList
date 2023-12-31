package com.example.mvvmtodolist.todo.content

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.mvvmtodolist.R
import com.example.mvvmtodolist.databinding.TodoAddActivityBinding
import com.example.mvvmtodolist.todo.home.TodoModel


class TodoContentActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TODO_ENTRY_TYPE = "extra_todo_entry_type"
        const val EXTRA_TODO_POSITION = "extra_todo_position"
        const val EXTRA_TODO_MODEL = "extra_todo_model"
        const val EXTRA_TODO_ISBOOKMARKED = "extra_todo_isbookmarked"

        fun newIntentForAdd(
            context: Context
        ) = Intent(context, TodoContentActivity::class.java).apply {
            putExtra(EXTRA_TODO_ENTRY_TYPE, TodoContentType.ADD.name)
        }

        fun newIntentForEdit(
            context: Context,
            position: Int,
            todoModel: TodoModel
        ) = Intent(context, TodoContentActivity::class.java).apply {
            putExtra(EXTRA_TODO_ENTRY_TYPE, TodoContentType.EDIT.name)
            putExtra(EXTRA_TODO_POSITION, position)
            putExtra(EXTRA_TODO_MODEL, todoModel)
        }
    }
    private lateinit var binding: TodoAddActivityBinding

    private val entryType by lazy {
        TodoContentType.from(intent.getStringExtra(EXTRA_TODO_ENTRY_TYPE))
    }


    private val todoModel by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra(
                EXTRA_TODO_MODEL,
                TodoModel::class.java
            )
        } else {
            intent?.getParcelableExtra<TodoModel>(
                EXTRA_TODO_MODEL
            )
        }
    }

    private val position by lazy {
        intent.getIntExtra(EXTRA_TODO_POSITION, -1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TodoAddActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initData()
    }

    private fun initView() = with(binding) {

        toolBar.setNavigationOnClickListener {
            finish()
        }

        submit.setOnClickListener {
            val intent = Intent().apply {
                putExtra(
                    EXTRA_TODO_ENTRY_TYPE,
                    entryType?.name
                )
                putExtra(
                    EXTRA_TODO_POSITION,
                    position
                )
                putExtra(
                    EXTRA_TODO_MODEL,
                    todoModel?.copy(
                        title = todoTitle.text.toString(),
                        description = todoDescription.text.toString()
                    )
                )
            }
            setResult(Activity.RESULT_OK, intent)
            finish()

        }

        delete.setOnClickListener {
            AlertDialog.Builder(this@TodoContentActivity).apply {
                setMessage(R.string.todo_add_delete_dialog_message)
                setPositiveButton(
                    R.string.todo_add_delete_dialog_positive
                ) { _, _ ->
                    val intent = Intent().apply {
                        putExtra(
                            EXTRA_TODO_ENTRY_TYPE,
                            TodoContentType.REMOVE.name
                        )
                        putExtra(
                            EXTRA_TODO_POSITION,
                            position
                        )
                        putExtra(
                            EXTRA_TODO_MODEL,
                            todoModel
                        )
                    }
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
                setNegativeButton(
                    R.string.todo_add_delete_dialog_negative
                ) { _, _ ->
                    // nothing
                }
            }.create().show()
        }

        // 버튼 이름 변경
        submit.setText(
            when (entryType) {
                TodoContentType.EDIT -> R.string.todo_add_edit
                else -> R.string.todo_add_submit
            }
        )

        // 추가 버튼이 아닐 경우 삭제 버튼 노출
        delete.isVisible = entryType != TodoContentType.ADD
    }

    private fun initData() = with(binding) {
        if (entryType == TodoContentType.EDIT) {
            todoTitle.setText(todoModel?.title)
            todoDescription.setText(todoModel?.description)
        }
    }
}