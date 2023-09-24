package com.example.mvvmtodolist.main

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.mvvmtodolist.R
import com.example.mvvmtodolist.bookmark.BookmarkFragment
import com.example.mvvmtodolist.bookmark.BookmarkModel
import com.example.mvvmtodolist.bookmark.toTodoModel
import com.example.mvvmtodolist.databinding.MainActivityBinding
import com.example.mvvmtodolist.todo.content.TodoContentActivity
import com.example.mvvmtodolist.todo.home.TodoFragment
import com.example.mvvmtodolist.todo.home.TodoModel
import com.example.mvvmtodolist.todo.home.toBookmarkModel
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    private val viewPagerAdapter by lazy {
        MainViewPagerAdapter(this@MainActivity)
    }

    private val viewModel : SharedViewModel by viewModels()

    private val addTodoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
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
                val todoFragment = viewPagerAdapter.getFragment(0) as? TodoFragment
                todoFragment?.setDodoContent(todoModel?.copy(isBookmark = false))
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initModel()
    }

    private fun initModel() = with(viewModel){

    }

    private fun initView() = with(binding) {
        toolBar.title = getString(R.string.app_name)

        viewPager.adapter = viewPagerAdapter
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (viewPagerAdapter.getFragment(position) is TodoFragment) {
                    fabAddTodo.show()
                } else {
                    fabAddTodo.hide()
                }
            }
        })

        // TabLayout x ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setText(viewPagerAdapter.getTitle(position))
        }.attach()

        // fab
        fabAddTodo.setOnClickListener {
            addTodoLauncher.launch(
                TodoContentActivity.newIntentForAdd(this@MainActivity)
            )
        }
    }

}