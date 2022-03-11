package ows.kotlinstudy.movierank_application.presentation.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.android.scope.ScopeFragment
import ows.kotlinstudy.movierank_application.databinding.FragmentMypageBinding
import ows.kotlinstudy.movierank_application.domain.model.ReviewedMovie
import ows.kotlinstudy.movierank_application.extension.dip
import ows.kotlinstudy.movierank_application.extension.toGone
import ows.kotlinstudy.movierank_application.extension.toVisible
import ows.kotlinstudy.movierank_application.presentation.home.GridSpacingItemDecoration

class MyPageFragment : ScopeFragment(), MyPageContract.View {

    override val presenter: MyPageContract.Presenter by inject()

    private var binding: FragmentMypageBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentMypageBinding.inflate(inflater, container, false)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        bindView()
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

    override fun showNoDataDescription(message: String) {
        binding?.recyclerView?.toGone()
        binding?.descriptionTextView?.toVisible()
        binding?.descriptionTextView?.text = message
    }

    override fun showErrorDescription(message: String) {
        binding?.recyclerView?.toGone()
        binding?.descriptionTextView?.toVisible()
        binding?.descriptionTextView?.text = message
    }

    override fun showReviewedMovies(reviewedMovies: List<ReviewedMovie>) {
        (binding?.recyclerView?.adapter as? MyPageAdapter)?.apply{
            this.reviewdMovies = reviewedMovies
            notifyDataSetChanged()
        }
    }

    private fun initViews(){
        binding?.recyclerView?.apply {
            adapter = MyPageAdapter()
            layoutManager = GridLayoutManager(context, 3, RecyclerView.VERTICAL, false)
            addItemDecoration(GridSpacingItemDecoration(3, dip(6f)))
        }
    }

    private fun bindView(){
        (binding?.recyclerView?.adapter as? MyPageAdapter)?.apply{
            onMovieClickListener = { movie ->
                val action = MyPageFragmentDirections.toMovieReviewsAction(movie)
                findNavController().navigate(action)
            }
        }
    }





}