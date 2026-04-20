package com.intercom.posts.presentation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.intercom.posts.R
import com.intercom.posts.databinding.ActivityPostDetailBinding
import com.intercom.posts.domain.model.CommentDto
import com.intercom.posts.domain.model.CommentUserDto
import com.intercom.posts.domain.model.CommentsResponseDto
import com.intercom.posts.presentation.adapter.CommentsAdapter
import com.intercom.posts.presentation.uiState.UiState
import com.intercom.posts.presentation.viewmodel.CommentsViewModel
import kotlinx.coroutines.launch

/**
 * Activity to display post details and comments
 * Receives post information via intent and displays all post details
 * Also displays comments for the post using a RecyclerView with CommentsAdapter
 */
class PostDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostDetailBinding
    private lateinit var commentsAdapter: CommentsAdapter
    private lateinit var commentsViewModel: CommentsViewModel
    private var postId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup edge-to-edge display
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Get post information from intent
        postId = intent.getIntExtra(EXTRA_POST_ID, -1)
        val postTitle = intent.getStringExtra("post_title") ?: ""
        val postBody = intent.getStringExtra("post_body") ?: ""
        val postViews = intent.getIntExtra("post_views", 0)
        val postLikes = intent.getIntExtra("post_likes", 0)
        val postDislikes = intent.getIntExtra("post_dislikes", 0)
        val postTags = intent.getStringArrayExtra("post_tags") ?: arrayOf()

        if (postId == -1) {
            finish()
            return
        }

        setupCommentsViewModel()

        // Setup UI components
        setupToolbar()
        setupCommentsRecyclerView()
        displayPostDetail(postTitle, postBody, postViews, postLikes, postDislikes, postTags)
    }

    /**
     * Setup toolbar with back navigation
     */
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = getString(R.string.post_detail)
        }
    }

    /**
     * Setup RecyclerView for displaying comments
     */
    private fun setupCommentsRecyclerView() {
        // Initialize the comments adapter
        commentsAdapter = CommentsAdapter()
        binding.commentsRecyclerView.adapter = commentsAdapter
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /**
     * Display post details on the UI
     */
    private fun displayPostDetail(
        title: String,
        body: String,
        views: Int,
        likes: Int,
        dislikes: Int,
        tags: Array<String>
    ) {
        // Set post title
        binding.postDetailTitleTextView.text = title
        
        // Set post body content
        binding.postDetailBodyTextView.text = body
        
        // Set post statistics
        binding.postDetailViewsTextView.text = "Views: $views"
        binding.postDetailLikesTextView.text = "Likes: $likes"
        binding.postDetailDislikesTextView.text = "Dislikes: $dislikes"

        // Display tags - join array with comma separator
        val tagsText = tags.joinToString(", ")
        binding.postDetailTagsTextView.text = "Tags: $tagsText"
    }


    private fun setupCommentsViewModel() {
        commentsViewModel = ViewModelProvider(this)[CommentsViewModel::class.java]
        getComments()
        observeCommentsUiState()
    }

    private fun getComments() {
        commentsViewModel.getComments(postId)
    }
    fun observeCommentsUiState(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                commentsViewModel.state.collect { state ->
                    when (state) {
                        is UiState.Loading -> handleCommentsLoadingState()
                        is UiState.Success -> handleCommentsSuccessState(state.data)
                        is UiState.Error -> handleCommentsErrorState(state.message)
                        else -> {}
                    }
                }
            }
        }
    }

    /**
     * Handle error state for comments - show error message
     */
    fun handleCommentsErrorState(message: String) {
        binding.commentsProgressBar.visibility = android.view.View.GONE
        binding.commentsErrorTextView.visibility = android.view.View.VISIBLE
        binding.commentsRecyclerView.visibility = android.view.View.GONE
        binding.commentsErrorTextView.text = message
    }

    /**
     * Handle success state for comments - display comments list or empty state
     */
    private fun handleCommentsSuccessState(data: CommentsResponseDto) {
        binding.commentsProgressBar.visibility = android.view.View.GONE
        binding.commentsErrorTextView.visibility = android.view.View.GONE
        
        // Check if comments list is empty
        if (data.comments.isEmpty()) {
            binding.commentsRecyclerView.visibility = android.view.View.GONE
            binding.commentsEmptyTextView.visibility = android.view.View.VISIBLE
        } else {
            binding.commentsRecyclerView.visibility = android.view.View.VISIBLE
            binding.commentsEmptyTextView.visibility = android.view.View.GONE
            displayComments(data.comments)
        }
    }

    /**
     * Handle loading state for comments - show progress bar
     */
    private fun handleCommentsLoadingState() {
        binding.commentsProgressBar.visibility = android.view.View.VISIBLE
        binding.commentsErrorTextView.visibility = android.view.View.GONE
        binding.commentsRecyclerView.visibility = android.view.View.GONE
    }

    /**
     * Display comments in the RecyclerView
     * @param comments List of comment objects to display
     */
    private fun displayComments(comments: List<CommentDto>) {
        // Submit the list to the adapter for display
        commentsAdapter.submitList(comments)
    }

    companion object {
        const val EXTRA_POST_ID = "com.intercom.posts.POST_ID"
    }
}

