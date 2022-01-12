package ows.kotlinstudy.todo_application.presentation.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ows.kotlinstudy.todo_application.R
import ows.kotlinstudy.todo_application.data.entity.ToDoEntity
import ows.kotlinstudy.todo_application.databinding.ViewholderTodoItemBinding

class ToDoAdapter : RecyclerView.Adapter<ToDoAdapter.ToDoItemViewHolder>() {

    private var toDoList: List<ToDoEntity> = listOf()
    private lateinit var toDoItemClickListener: (ToDoEntity) -> Unit
    private lateinit var toDoCheckListener: (ToDoEntity) -> Unit

    inner class ToDoItemViewHolder(
        private val binding: ViewholderTodoItemBinding,
        val toDoItemClickListener: (ToDoEntity) -> Unit
    ): RecyclerView.ViewHolder(binding.root){

        fun bindData(data: ToDoEntity) = with(binding){
            checkBox.text = data.title
            checkToDoComplete(data.hasCompleted)
        }

        fun bindView(data: ToDoEntity){
            binding.checkBox.setOnClickListener {
                toDoCheckListener(
                    data.copy(hasCompleted = binding.checkBox.isChecked)
                )
                checkToDoComplete(binding.checkBox.isChecked)
            }
            binding.root.setOnClickListener {
                toDoItemClickListener(data)
            }
        }

        fun checkToDoComplete(isChecked: Boolean) = with(binding){
            checkBox.isChecked = isChecked
            container.setBackgroundColor(
                ContextCompat.getColor(
                    root.context,
                    if(isChecked){
                        R.color.gray_300
                    }else {
                        R.color.white
                    }
                )
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoItemViewHolder {
        Log.d("msg","onCreateViewHolder")
        var view = ViewholderTodoItemBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ToDoItemViewHolder(view, toDoItemClickListener)
    }

    override fun onBindViewHolder(holder: ToDoItemViewHolder, position: Int) {
        holder.bindData(toDoList[position])
        holder.bindView(toDoList[position])
    }

    override fun getItemCount(): Int = toDoList.size

    fun setToDoList(toDoList: List<ToDoEntity>, toDoItemClickListener: (ToDoEntity) -> Unit, toDoCheckListener: (ToDoEntity) -> Unit){
        this.toDoList = toDoList
        this.toDoItemClickListener = toDoItemClickListener
        this.toDoCheckListener = toDoCheckListener
        notifyDataSetChanged()
    }
}