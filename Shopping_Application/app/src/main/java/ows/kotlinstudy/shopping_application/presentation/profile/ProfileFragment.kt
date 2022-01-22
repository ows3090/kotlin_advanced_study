package ows.kotlinstudy.shopping_application.presentation.profile

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import org.koin.android.ext.android.inject
import ows.kotlinstudy.shopping_application.R
import ows.kotlinstudy.shopping_application.databinding.FragmentProfileBinding
import ows.kotlinstudy.shopping_application.extension.loadCenterCrop
import ows.kotlinstudy.shopping_application.extension.toast
import ows.kotlinstudy.shopping_application.presentation.BaseFragment
import ows.kotlinstudy.shopping_application.presentation.adapter.ProductListAdapter
import ows.kotlinstudy.shopping_application.presentation.detail.ProductDetailActivity

internal class ProfileFragment : BaseFragment<ProfileViewModel, FragmentProfileBinding>() {

    companion object {
        const val TAG = "ProfileFragment"
    }

    override val viewModel: ProfileViewModel by inject<ProfileViewModel>()

    override fun getViewBinding(): FragmentProfileBinding =
        FragmentProfileBinding.inflate(layoutInflater)

    private val gso: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.firebase_web_client_id))
            .requestEmail()
            .build()
    }

    private val gsc by lazy { GoogleSignIn.getClient(requireActivity(), gso) }

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val adapter = ProductListAdapter()

    private val loginLaucher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    task.getResult(ApiException::class.java)?.let {
                        Log.e(TAG, "firebasewithGoogle : ${it.id}")
                        viewModel.saveToken(it.idToken ?: throw Exception())
                    } ?: throw Exception()
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }

    override fun observeData() = viewModel.profileStateLiveData.observe(this){
        when(it){
            is ProfileState.Uninitialized -> initViews()
            is ProfileState.Loading -> handleLoadingState()
            is ProfileState.Login -> handleLoginState(it)
            is ProfileState.Success -> handleSuccessState(it)
            is ProfileState.Error -> handleErrorState()
        }
    }

    private fun initViews() = with(binding) {
        recyclerView.adapter = adapter
        loginButton.setOnClickListener {
            signInGoogle()
        }

        logoutButton.setOnClickListener {
            viewModel.signOut()
        }
    }

    private fun signInGoogle() {
        val signInInent = gsc.signInIntent
        loginLaucher.launch(signInInent)
    }

    private fun handleLoadingState() = with(binding){
        progressBar.isVisible = true
        loginRequiredGroup.isGone = true
    }

    private fun handleSuccessState(state: ProfileState.Success) = with(binding){
        progressBar.isGone = true
        when(state){
            is ProfileState.Success.Registered -> {
                handleRegisteredState(state)
            }
            is ProfileState.Success.NotRegistered -> {
                profileGroup.isGone = true
                loginRequiredGroup.isVisible = true
            }
        }
    }

    private fun handleLoginState(state: ProfileState.Login) = with(binding){
        progressBar.isVisible = true
        val credential = GoogleAuthProvider.getCredential(state.idToken,null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()){ task->
                if(task.isSuccessful){
                    val user = firebaseAuth.currentUser
                    viewModel.setUserInfo(user)
                }else{
                    viewModel.setUserInfo(null)
                }
            }
    }

    private fun handleRegisteredState(state: ProfileState.Success.Registered) = with(binding){
        profileGroup.isVisible = true
        loginRequiredGroup.isGone = true
        profileImageView.loadCenterCrop(state.profileImageUrl.toString(), 60f)
        userNameTextView.text = state.userName

        if(state.productList.isEmpty()){
            emptyResultTextView.isGone = false
            recyclerView.isGone = true
        }else{
            emptyResultTextView.isGone = true
            recyclerView.isGone = false
            adapter.setProductList(state.productList){
                startActivity(
                    ProductDetailActivity.newIntent(requireContext(), it.id)
                )
            }
        }
    }

    private fun handleErrorState(){
        requireActivity().toast("에러가 발생했습니다")
    }



}