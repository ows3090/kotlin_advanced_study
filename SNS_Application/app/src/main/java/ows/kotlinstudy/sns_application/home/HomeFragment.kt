package ows.kotlinstudy.sns_application.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ows.kotlinstudy.sns_application.DBKey.Companion.CHILD_CHAT
import ows.kotlinstudy.sns_application.DBKey.Companion.DB_ARTICLES
import ows.kotlinstudy.sns_application.DBKey.Companion.DB_USERS
import ows.kotlinstudy.sns_application.R
import ows.kotlinstudy.sns_application.chatlist.ChatListItem
import ows.kotlinstudy.sns_application.databinding.FragmentHomeBinding

class HomeFragment: Fragment(R.layout.fragment_home) {

    private lateinit var articleDB: DatabaseReference
    private lateinit var userDB: DatabaseReference
    private lateinit var articleAdapter: ArticleAdapter

    private val articleList = mutableListOf<ArticleModel>()
    private val listener = object: ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val articleModel = snapshot.getValue(ArticleModel::class.java)
            articleModel ?: return

            articleList.add(articleModel)
            articleAdapter.submitList(articleList)
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

        override fun onChildRemoved(snapshot: DataSnapshot) {}

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

        override fun onCancelled(error: DatabaseError) {}
    }

    // Fragment??? ??????????????? View??? ?????????????????? ?????? ??????

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        articleList.clear()

        // ?????? ?????? RecyclerVeiw Adapter
        articleAdapter = ArticleAdapter(
            onItemClicked = { articleModel ->
                if(auth.currentUser != null){
                    auth.currentUser?.uid?.let {
                        // ?????? ??????????????? ????????? ?????? ?????? ??????
                        if (auth.currentUser?.uid != articleModel.userId) {
                            val chatRoom = ChatListItem(
                                buyerId = it,
                                sellerId = articleModel.userId,
                                itemTitle = articleModel.title,
                                key = System.currentTimeMillis()
                            )

                            userDB.child(it).child(CHILD_CHAT).push().setValue(chatRoom)
                            userDB.child(articleModel.userId).child(CHILD_CHAT).push().setValue(chatRoom)

                            Snackbar.make(view, "???????????? ?????????????????????. ??????????????? ??????????????????", Snackbar.LENGTH_LONG).show()
                        }
                        else{
                            Snackbar.make(view, "?????? ?????? ??????????????????.", Snackbar.LENGTH_LONG).show()
                        }
                    }
                } else{
                    Snackbar.make(view, "????????? ??? ??????????????????", Snackbar.LENGTH_LONG).show()
                }
            }
        )
        initFirebase()
        initViews()
        bindViews()
    }

    private fun initFirebase(){
        userDB = Firebase.database.reference.child(DB_USERS)
        articleDB = Firebase.database.reference.child(DB_ARTICLES)

        // addListenerForSingleValueEvent() : ?????????, 1?????? ??????
        // addChildEventListener() : ???????????? ????????? ????????? ??????
        articleDB.addChildEventListener(listener)
    }

    private fun initViews() = with(binding!!){
        articleRecyclerView.layoutManager = LinearLayoutManager(context)
        articleRecyclerView.adapter = articleAdapter
    }

    private fun bindViews() = with(binding!!){
        addFloatingButton.setOnClickListener {
            context?.let{
                if(auth.currentUser != null){
                    // ?????? ?????? Activity Start
                    startActivity(Intent(requireContext(), ArticleAddActivity::class.java))
                } else {
                    Snackbar.make(root, "????????? ??? ??????????????????", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        articleAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        articleDB.removeEventListener(listener)
    }
}