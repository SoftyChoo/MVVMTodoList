package com.example.mvvmtodolist.bookmark

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.mvvmtodolist.databinding.BookmarkFragmentBinding
import com.example.mvvmtodolist.main.MainActivity
import com.example.mvvmtodolist.main.MainSharedEventForBookmark
import com.example.mvvmtodolist.main.MainSharedViewModel
import com.example.mvvmtodolist.todo.content.TodoContentActivity

class BookmarkFragment : Fragment() {

    companion object {
        fun newInstance() = BookmarkFragment()
    }

    private var _binding: BookmarkFragmentBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: MainSharedViewModel by activityViewModels()
    private val viewModel: BookmarkViewModel by viewModels()

    private val listAdapter by lazy {
        BookmarkListAdapter { position, item ->
            viewModel.removeBookmarkItem(position)
            sharedViewModel.updateTodoItem(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BookmarkFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {
        bookmarkList.adapter = listAdapter
    }

    private fun initViewModel() {
        with(viewModel) {
            list.observe(viewLifecycleOwner) {
                listAdapter.submitList(it)
            }
        }

        with(sharedViewModel) {
            bookmarkEvent.observe(viewLifecycleOwner) { event ->
                when (event) {
                    is MainSharedEventForBookmark.UpdateBookmarkItems -> {
                        viewModel.updateBookmarkItems(event.items)
                    }

                    else -> Unit
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}