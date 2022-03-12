package ows.kotlinstudy.movierank_application.presentation.reviews

import android.app.AlertDialog
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.android.scope.ScopeFragment
import org.koin.core.parameter.parametersOf
import ows.kotlinstudy.movierank_application.databinding.FragmentMoviereviewBinding
import ows.kotlinstudy.movierank_application.domain.model.Movie
import ows.kotlinstudy.movierank_application.domain.model.MovieReviews
import ows.kotlinstudy.movierank_application.domain.model.Review
import ows.kotlinstudy.movierank_application.extension.toGone
import ows.kotlinstudy.movierank_application.extension.toVisible

class MovieReviewsFragment : ScopeFragment(), MovieReviewsContract.View {

    override val presenter: MovieReviewsContract.Presenter by inject { parametersOf(arguments.movie) }

    private val arguments: MovieReviewsFragmentArgs by navArgs()
    private var binding: FragmentMoviereviewBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentMoviereviewBinding.inflate(inflater, container, false)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        presenter.onViewCreated()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestoryView()
        binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestory()
    }

    override fun showLoadingIndicator() {
        binding?.progressBar?.toVisible()
    }

    override fun hideLoadingIndicator() {
        binding?.progressBar?.toGone()
    }

    override fun showErrorDescription(message: String) {
        binding?.recyclerView?.toGone()
        binding?.errorDescriptionTextView?.toVisible()
        binding?.errorDescriptionTextView?.text = message
    }

    override fun showMovieInformation(movie: Movie) {
        binding?.recyclerView?.adapter = MovieReviewsAdapter(movie).apply {
            onReviewSubmitButtonClickListener = { content, score ->
                presenter.requestAddReview(content, score)
                hideKeyboard()
            }
            onReviewDeleteButtonClickListener = { review ->
                showDeleteConfirmDialog(review)
            }
        }
    }

    private fun showDeleteConfirmDialog(review: Review) {
        AlertDialog.Builder(requireContext()!!)
            .setMessage("정말로 리뷰를 삭제하시겠어요?")
            .setPositiveButton("삭제할래요") { _, _ ->
                presenter.requestRemoveReview(review)
            }
            .setNegativeButton("안할래요") { _, _ -> }
            .show()
    }

    override fun showReviews(reviews: MovieReviews) {
        binding?.recyclerView?.toVisible()
        binding?.errorDescriptionTextView?.toGone()
        (binding?.recyclerView?.adapter as MovieReviewsAdapter)?.apply {
            this.myReview = reviews.myReview
            this.reviews = reviews.othersReviews
            notifyDataSetChanged()
        }
    }

    override fun showErrorToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun initViews() {
        binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(
                this.context,
                RecyclerView.VERTICAL,
                false
            )
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }


}