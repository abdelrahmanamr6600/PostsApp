package com.intercom.posts.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.intercom.posts.R
import com.intercom.posts.databinding.ActivityMainBinding
import com.intercom.posts.presentation.adapter.UsersAdapter
import com.intercom.posts.presentation.uiState.UiState
import com.intercom.posts.presentation.utils.LoadingDialogHelper
import com.intercom.posts.presentation.viewmodel.UsersViewModel
import com.intercom.posts.domain.model.UserDto
import kotlinx.coroutines.launch

/**
 * MainActivity - Entry point of the application
 * Displays list of users in RecyclerView
 * Handles navigation to UserDetailActivity on user selection
 */
class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var usersViewModel: UsersViewModel
    private lateinit var usersAdapter: UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupViewModel()
        setupRecyclerView()
        observeUiState()
    }

    /**
     * Initialize ViewModel
     */
    private fun setupViewModel() {
        usersViewModel = ViewModelProvider(this)[UsersViewModel::class.java]
    }

    /**
     * Setup RecyclerView with adapter
     */
    private fun setupRecyclerView() {
        usersAdapter = UsersAdapter { user ->
            onUserSelected(user)
        }
        binding.usersRecyclerView.adapter = usersAdapter
    }

    /**
     * Observe UI state from ViewModel
     * Handle Loading, Success, and Error states
     */
    private fun observeUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                usersViewModel.state.collect { state ->
                    when (state) {
                        is UiState.Loading -> handleLoadingState()
                        is UiState.Success -> handleSuccessState(state.data)
                        is UiState.Error -> handleErrorState(state.message)
                        else -> {}
                    }
                }
            }
        }
    }

    /**
     * Handle loading state - show progress dialog
     */
    private fun handleLoadingState() {
        LoadingDialogHelper.showLoadingDialog(this)
        binding.usersRecyclerView.visibility = View.GONE
        binding.errorTextView.visibility = View.GONE
    }

    /**
     * Handle success state - display users list
     */
    private fun handleSuccessState(users: List<UserDto>) {
        LoadingDialogHelper.hideLoadingDialog()
        binding.usersRecyclerView.visibility = View.VISIBLE
        binding.errorTextView.visibility = View.GONE
        usersAdapter.submitList(users)
    }

    /**
     * Handle error state - display error message
     */
    private fun handleErrorState(message: String) {
        LoadingDialogHelper.hideLoadingDialog()
        binding.usersRecyclerView.visibility = View.GONE
        binding.errorTextView.visibility = View.VISIBLE
        binding.errorTextView.text = message
    }

    /**
     * Navigate to UserDetailActivity with user ID
     * @param user - Selected user
     */
    private fun onUserSelected(user: UserDto) {
        val intent = Intent(this, UserDetailActivity::class.java).apply {
            putExtra(UserDetailActivity.EXTRA_USER_ID, user.id)
            putExtra("user_name", "${user.firstName} ${user.lastName}")
            putExtra("user_image", user.image)
        }
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        LoadingDialogHelper.hideLoadingDialog()
    }
}

