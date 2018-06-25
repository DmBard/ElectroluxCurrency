package com.baryshev.currency.utils.extensions

import android.content.Context
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.View
import com.baryshev.currency.R
import com.baryshev.currency.utils.loge

inline fun Context.makeErrorSnackBar(parent: View,
                                     error: Throwable?,
                                     crossinline action: () -> Unit,
                                     text: String = getString(R.string.error_common),
                                     actionString: String? = getString(R.string.snackbar_action_retry)
): Snackbar {
    loge("Error!",
         "expand error",
         error ?: UnknownError())

    return Snackbar.make(parent, text, Snackbar.LENGTH_INDEFINITE)
        .setAction(actionString) { action() }
        .setActionTextColor(ContextCompat.getColor(this, R.color.colorAccent))

}
