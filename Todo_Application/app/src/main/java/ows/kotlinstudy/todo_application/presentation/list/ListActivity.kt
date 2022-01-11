package ows.kotlinstudy.todo_application.presentation.list

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.android.ext.android.bind
import ows.kotlinstudy.todo_application.R
import ows.kotlinstudy.todo_application.databinding.ActivityListBinding
import ows.kotlinstudy.todo_application.presentation.BaseActivity
import kotlin.coroutines.CoroutineContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.Error

internal class ListActivity : BaseActivity<ListViewModel>(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private lateinit var binding: ActivityListBinding

    override val viewModel: ListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

    private fun initViews() = with(binding){
        recyclerView.layoutManager = LinearLayoutManager(this@ListActivity, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adpter

        refreshLayout.setOnRefreshListener {
            viewModel.fetchData()
        }

        addToDoButton.setOnClickListener{
            startActivityForResult(

            )
        }
    }

    override fun observeData() {
        viewModel.toDoListLiveData.observe(this){
            when(it){
                is ToDoListState.UnInitialized ->{
                    initViews()
                }
                is ToDoListState.Loading -> {
                    handleLoadingState()
                }
                is ToDoListState.Success -> {
                    handleSuccessState(it)
                }
                is ToDoListState.Error -> {
                    handleErrorState()
                }
            }
        }
    }



}