package com.intercom.posts.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.intercom.posts.R
import com.intercom.posts.databinding.ActivityUserDetailBinding
import com.intercom.posts.presentation.adapter.PostsAdapter
import com.intercom.posts.presentation.viewmodel.PostsViewModel
import com.intercom.posts.domain.model.PostDto
import com.intercom.posts.domain.model.PostResponseDto
import com.intercom.posts.presentation.uiState.UiState
import com.intercom.posts.presentation.utils.LoadingDialogHelper
import kotlinx.coroutines.launch


/**
 * Activity to display user details
 * Receives user id and user information via intent and displays user details
 * Shows user's posts in a RecyclerView
 */
class UserDetailActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var postsViewModel : PostsViewModel
    private lateinit var postsAdapter: PostsAdapter
    private var userId: Int = -1

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         enableEdgeToEdge()
         
         binding = ActivityUserDetailBinding.inflate(layoutInflater)
         setContentView(binding.root)

         // Setup window insets for edge-to-edge display
         ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.user_detail_container)) { v, insets ->
             val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
             v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
             insets
         }

         // Get user information from intent
        userId = intent.getIntExtra(EXTRA_USER_ID, -1)
        val userName = intent.getStringExtra("user_name") ?: ""
        val userImage = intent.getStringExtra("user_image") ?: ""
        
        if (userId == -1) {
            finish()
            return
        }
        Log.d("UserDetailActivity", "Received userId: $userId, userName: $userName")

        setupToolbar()
        setupViewModel()
        getPostById()
        observePostsUiState()
        displayUserDetail(userName, userImage)
        setupRecyclerView()

    }

    private fun getPostById() {
        postsViewModel.getPosts(userId)

    }

    /**
     * Setup toolbar with back navigation
     */
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = getString(R.string.user_detail)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /**
     * Display user details on the UI
     */
    private fun displayUserDetail(userName: String, userImage: String) {
        binding.userNameDetailTextView.text = userName
        Glide.with(this)
            .load(userImage)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(binding.userDetailImageView)
    }

    /**
     * Setup RecyclerView with posts adapter
     */
    private fun setupRecyclerView() {
        postsAdapter = PostsAdapter { post ->
            onPostSelected(post)
        }
        binding.postsRecyclerView.adapter = postsAdapter
    }

    /**
     * Setup ViewModel - will be used to fetch posts data
     */
    private fun setupViewModel() {
        postsViewModel = ViewModelProvider(this)[PostsViewModel::class.java]

    }

    /**
     * Navigate to PostDetailsActivity with post information
     */
    private fun onPostSelected(post: PostDto) {
        val intent = Intent(this, PostDetailsActivity::class.java).apply {
            putExtra(PostDetailsActivity.EXTRA_POST_ID, post.id)
            putExtra("post_title", post.title)
            putExtra("post_body", post.body)
            putExtra("post_views", post.views)
            putExtra("post_likes", post.reactions.likes)
            putExtra("post_dislikes", post.reactions.dislikes)
            putExtra("post_tags", post.tags.toTypedArray())
        }
        startActivity(intent)
    }

    private fun observePostsUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                postsViewModel.state.collect { state ->
                    when (state) {
                        is UiState.Loading -> handleLoadingState()
                        is UiState.Success -> handleSuccessState(state.data)
                        is UiState.Error -> handleErrorState(state.message)
                        is UiState.Empty -> handleEmptyState()
                    }
                }
            }
        }
    }
    private fun handleLoadingState(){
        LoadingDialogHelper.showLoadingDialog(this)
        binding.postsRecyclerView.visibility = View.GONE
    }

    private fun handleSuccessState(respons: PostResponseDto){
        LoadingDialogHelper.hideLoadingDialog()
        binding.postsRecyclerView.visibility = View.VISIBLE
        postsAdapter.submitList(respons.posts)

        binding.totalPostsTextView.text = respons.total.toString()

    }

    private fun handleErrorState(message: String){
        LoadingDialogHelper.hideLoadingDialog()
        binding.postsRecyclerView.visibility = View.GONE
        binding.errorTextView.visibility = View.VISIBLE
        binding.errorTextView.text = message
    }

    private fun handleEmptyState(){
        LoadingDialogHelper.hideLoadingDialog()
        binding.postsRecyclerView.visibility = View.GONE
        binding.errorTextView.visibility = View.VISIBLE
        binding.errorTextView.text = getString(R.string.no_posts_available)
    }


    companion object {
        const val EXTRA_USER_ID = "com.intercom.posts.USER_ID"
    }
}




