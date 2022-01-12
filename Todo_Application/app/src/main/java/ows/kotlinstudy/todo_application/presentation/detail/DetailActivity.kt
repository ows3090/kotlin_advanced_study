package ows.kotlinstudy.todo_application.presentation.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isGone
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ows.kotlinstudy.todo_application.databinding.ActivityDetailBinding
import ows.kotlinstudy.todo_application.presentation.BaseActivity

internal class DetailActivity : BaseActivity<DetailViewModel>() {

    override val viewModel: DetailViewModel by viewModel{
        parametersOf(
            intent.getSerializableExtra(DETAIL_MODE_KEY),
            intent.getLongExtra(TODO_ID_KEY,-1)
        )
    }

    companion object{
        const val TODO_ID_KEY = "ToDoId"
        const val DETAIL_MODE_KEY = "DetailMode"

        const val FETCH_REQUEST_CODE = 10

        fun getIntent(context: Context, detailMode: DetailMode) = Intent(context, DetailActivity::class.java).apply {
            putExtra(DETAIL_MODE_KEY, detailMode)
        }

        fun getIntent(context: Context, id: Long, detailMode: DetailMode) = Intent(context, DetailActivity::class.java).apply {
            putExtra(TODO_ID_KEY, id)
            putExtra(DETAIL_MODE_KEY, detailMode)
        }

    }

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setResult(RESULT_OK)
    }

    override fun observeData() {
        viewModel.toDoDetailLiveData.observe(this){
            when(it){
                is ToDoDetailState.UnInitialized -> {
                    initViews()
                }
                is ToDoDetailState.Loading -> {
                    handleLoadingState()
                }
                is ToDoDetailState.Success -> {
                    handleSuccessState(it)
                }
                is ToDoDetailState.Modify -> {
                    handleModifyState()
                }
                is ToDoDetailState.Delete -> {
                    Toast.makeText(this, "성공적으로 삭제", Toast.LENGTH_SHORT).show()
                    finish()
                }
                is ToDoDetailState.Error -> {
                    Toast.makeText(this, "에러 발생", Toast.LENGTH_SHORT).show()
                    finish()
                }
                is ToDoDetailState.Write -> {
                    handleWriteState()
                }
            }
        }
    }

    private fun initViews() = with(binding){
        titleInput.isEnabled = true
        descriptionInput.isEnabled = true

        deleteButton.isGone = true
        modifyButton.isGone = true
        updateButton.isGone = true

        deleteButton.setOnClickListener {
            viewModel.deleteToDo()
        }

        modifyButton.setOnClickListener {
            viewModel.setModifyMode()
        }

        updateButton.setOnClickListener {
            viewModel.writeToDo(
                title = titleInput.text.toString(),
                description = descriptionInput.text.toString()
            )
        }
    }

    private fun handleLoadingState() = with(binding){
        progressBar.isGone = false
    }

    private fun handleModifyState() = with(binding){
        titleInput.isEnabled = true
        descriptionInput.isEnabled = true

        deleteButton.isGone = true
        modifyButton.isGone = true
        updateButton.isGone = false
    }

    private fun handleWriteState() = with(binding){
        titleInput.isEnabled = true
        descriptionInput.isEnabled = true

        updateButton.isGone = false
    }

    private fun handleSuccessState(state: ToDoDetailState.Success) = with(binding){
        progressBar.isGone = true

        titleInput.isEnabled = false
        descriptionInput.isEnabled = false

        deleteButton.isGone = false
        modifyButton.isGone = false
        updateButton.isGone = true

        val toDoItem = state.toDoItem
        titleInput.setText(toDoItem.title)
        descriptionInput.setText(toDoItem.description)
    }


}