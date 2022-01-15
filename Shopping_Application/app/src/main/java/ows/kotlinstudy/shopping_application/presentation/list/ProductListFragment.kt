package ows.kotlinstudy.shopping_application.presentation.list

import org.koin.android.ext.android.inject
import ows.kotlinstudy.shopping_application.databinding.FragmentProductListBinding
import ows.kotlinstudy.shopping_application.databinding.FragmentProfileBinding
import ows.kotlinstudy.shopping_application.presentation.BaseFragment
import ows.kotlinstudy.shopping_application.presentation.profile.ProfileViewModel

internal class ProductListFragment: BaseFragment<ProductListViewModel, FragmentProductListBinding>() {

    companion object{
        const val TAG = "productListFragment"
    }

    override val viewModel: ProductListViewModel by inject<ProductListViewModel>()

    override fun getViewBinding(): FragmentProductListBinding = FragmentProductListBinding.inflate(layoutInflater)

    override fun observeData() {

    }
}