package com.intercom.posts.presentation.utils

import android.app.Dialog
import android.content.Context
import android.view.Window
import com.intercom.posts.R

/**
 * Helper class to manage loading progress dialog
 * Provides methods to show and hide loading indicators
 */
object LoadingDialogHelper {
    private var loadingDialog: Dialog? = null

    /**
     * Shows a loading dialog with progress bar
     * @param context - Context for creating the dialog
     */
    fun showLoadingDialog(context: Context) {
        if (loadingDialog == null) {
            loadingDialog = Dialog(context).apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                setCancelable(false)
                setContentView(R.layout.dialog_loading)
                window?.setBackgroundDrawableResource(android.R.color.transparent)
            }
        }
        if (!loadingDialog!!.isShowing) {
            loadingDialog!!.show()
        }
    }

    /**
     * Hides the loading dialog
     */
    fun hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog!!.isShowing) {
            loadingDialog!!.dismiss()
            loadingDialog = null
        }
    }
}

