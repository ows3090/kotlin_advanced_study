package ows.kotlinstudy.shopping_application.presentation.profile

import android.app.Activity
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import ows.kotlinstudy.shopping_application.R
import ows.kotlinstudy.shopping_application.databinding.FragmentProfileBinding
import ows.kotlinstudy.shopping_application.presentation.BaseFragment

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

    private val loginLaucher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    task.getResult(ApiException::class.java)?.let {
                        Log.e(TAG, "firebasewithGoogle : ${it.id}")

                        // TODO saveToken
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
            is ProfileState.Login -> TODO()
            is ProfileState.Success -> handleSuccessState(it)
            is ProfileState.Error -> TODO()
        }
    }

    private fun initViews() = with(binding) {
        loginButton.setOnClickListener {
            signInGoogle()
        }

        logoutButton.setOnClickListener {

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
                // TODO
            }
            is ProfileState.Success.NotRegistered -> {
                profileGroup.isGone = true
                loginRequiredGroup.isVisible = true
            }
        }
    }

}