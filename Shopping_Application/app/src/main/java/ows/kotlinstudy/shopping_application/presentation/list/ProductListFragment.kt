package ows.kotlinstudy.shopping_application.presentation.list

import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import org.koin.android.ext.android.inject
import ows.kotlinstudy.shopping_application.databinding.FragmentProductListBinding
import ows.kotlinstudy.shopping_application.extension.toast
import ows.kotlinstudy.shopping_application.presentation.BaseFragment
import ows.kotlinstudy.shopping_application.presentation.adapter.ProductListAdapter
import ows.kotlinstudy.shopping_application.presentation.detail.ProductDetailActivity
import ows.kotlinstudy.shopping_application.presentation.main.MainActivity

internal class ProductListFragment: BaseFragment<ProductListViewModel, FragmentProductListBinding>() {

    companion object{
        const val TAG = "productListFragment"
    }

    override val viewModel: ProductListViewModel by inject<ProductListViewModel>()

    override fun getViewBinding(): FragmentProductListBinding = FragmentProductListBinding.inflate(layoutInflater)

    private val adapter = ProductListAdapter()

    private val startProductDetailResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result: ActivityResult ->
            // TODO 성공적으로 처리 완료 이후 동작

            if(result.resultCode == ProductDetailActivity.PRODUCT_ORDERED_RESULT_CODE){
                (requireActivity() as MainActivity).viewModel.refreshOrderList()
            }
        }

    override fun observeData() = viewModel.productListStateLiveData.observe(this){
        when(it){
            is ProductListState.UnInitialized -> {
                initViews()
            }
            is ProductListState.Loading -> {
                handleLoadingState()
            }
            is ProductListState.Success -> {
                handleSuccessState(it)
            }
            is ProductListState.Error -> {
                handleErrorState()
            }
        }
    }

    private fun initViews() = with(binding){
        recyclerView.adapter = adapter

        refreshLayout.setOnRefreshListener {
            viewModel.fetchData()
        }
    }

    private fun handleLoadingState() = with(binding){
        refreshLayout.isRefreshing = true
    }

    private fun handleSuccessState(state: ProductListState.Success) = with(binding){
        refreshLayout.isRefreshing = false

        if(state.productList.isEmpty()){
            emptyResultTextView.isGone = false
            recyclerView.isGone = true
        }else{
            emptyResultTextView.isGone = true
            recyclerView.isGone = false
            adapter.setProductList(state.productList){
                startProductDetailResult.launch(
                    ProductDetailActivity.newIntent(requireContext(), it.id)
                )
            }
        }
    }

    private fun handleErrorState() {
        Toast.makeText(requireContext(), "에러가 발생했습니", Toast.LENGTH_SHORT).show()
        
    }
}