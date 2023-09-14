package com.example.mvvmtodolist.bookmark

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.mvvmtodolist.databinding.BookmarkFragmentBinding
import com.example.mvvmtodolist.todo.content.TodoContentActivity

class BookmarkFragment : Fragment() {

    companion object {
        fun newInstance() = BookmarkFragment()
    }

    private var _binding: BookmarkFragmentBinding? = null
    private val binding get() = _binding!!

    private val listAdapter by lazy {
        BookmarkListAdapter { position, item ->
            Toast.makeText(context,"position",Toast.LENGTH_SHORT).show()
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

    }

    private fun initView() = with(binding) {
        bookmarkList.adapter = listAdapter
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private val editBookmarkLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val position = result.data?.getStringExtra(TodoContentActivity.EXTRA_TODO_POSITION)
                val bookmarkModel = result.data?.getStringExtra(TodoContentActivity.EXTRA_TODO_MODEL)
            }
        }
}