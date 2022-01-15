package ows.kotlinstudy.shopping_application.presentation.profile

import org.koin.android.ext.android.inject
import ows.kotlinstudy.shopping_application.databinding.FragmentProfileBinding
import ows.kotlinstudy.shopping_application.presentation.BaseFragment

internal class ProfileFragment: BaseFragment<ProfileViewModel, FragmentProfileBinding>() {

    companion object{
        const val TAG = "ProfileFragment"
    }

    override val viewModel: ProfileViewModel by inject<ProfileViewModel>()

    override fun getViewBinding(): FragmentProfileBinding = FragmentProfileBinding.inflate(layoutInflater)

    override fun observeData() {

    }
}