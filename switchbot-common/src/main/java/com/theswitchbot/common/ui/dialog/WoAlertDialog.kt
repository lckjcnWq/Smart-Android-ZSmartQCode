package com.theswitchbot.common.ui.dialog

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.theswitchbot.common.databinding.DialogAlertBinding
import com.theswitchbot.common.util.onClick
import com.theswitchbot.common.widget.dialog.CenterDialog

/**
 * Author zrh
 * Date 2022/4/29 4:08 下午
 * Description
 */
typealias SingleClickListener = (Dialog, Int) -> Unit

class WoAlertDialog(activity: Activity) : CenterDialog<DialogAlertBinding>(activity) {
    constructor(builder: Builder) : this(builder.activity) {
        builder.title?.let { setupTitle(it) }
        builder.message?.let { setMessage(it) }
        builder.customView?.let { setCustomView(it) }
        builder.negativeText?.let { setNegativeButton(it) }
        builder.positiveText?.let { setPositiveButton(it) }
        builder.onNegativeClick?.let { onNegative(it) }
        builder.onPositiveClick?.let { onPositive(it) }
        builder.onDismissListener?.let { setOnDismissListener(it) }
        setCancelable(builder.cancelable)
        autoDismiss = builder.autoDismiss
    }

    private var onPositiveClick: SingleClickListener? = null
    private var onNegativeClick: SingleClickListener? = null
    private var autoDismiss = true

    init {
        binding.btnNegative.onClick {
            if (autoDismiss) dismiss()
            onNegativeClick?.invoke(this, 0)
        }
        binding.btnPositive.onClick {
            if (autoDismiss) dismiss()
            onPositiveClick?.invoke(this, 0)
        }
    }

    fun setCustomView(view: View): WoAlertDialog {
        binding.messageLayout.removeAllViews()
        binding.messageLayout.addView(view)
        return this
    }

    fun setCustomView(@LayoutRes id: Int): WoAlertDialog {
        val view = LayoutInflater.from(activity).inflate(id, null)
        return setCustomView(view)
    }

    fun getCustomView(): View? {
        return binding.messageLayout.getChildAt(0)
    }

    fun setupTitle(title: Int): WoAlertDialog {
        return setupTitle(context.getString(title))
    }

    fun setupTitle(title: CharSequence): WoAlertDialog {
        binding.tvTitle.visibility = View.VISIBLE
        binding.tvTitle.text = title
        return this
    }

    fun setMessage(message: Int): WoAlertDialog {
        return setMessage(context.getString(message))
    }

    fun setMessage(message: CharSequence): WoAlertDialog {
        binding.tvMessage.visibility = View.VISIBLE
        binding.tvMessage.text = message
        return this
    }

    fun setNegativeButton(text: Int): WoAlertDialog {
        return setNegativeButton(context.getString(text))
    }

    fun setNegativeButton(text: CharSequence): WoAlertDialog {
        binding.btnNegative.visibility = View.VISIBLE
        binding.btnNegative.text = text
        return this
    }

    fun onNegative(onClick: SingleClickListener): WoAlertDialog {
        onNegativeClick = onClick
        return this
    }

    fun setPositiveButton(text: Int): WoAlertDialog {
        return setPositiveButton(context.getString(text))
    }

    fun setPositiveButton(text: CharSequence): WoAlertDialog {
        binding.btnPositive.visibility = View.VISIBLE
        binding.btnPositive.text = text
        return this
    }

    fun onPositive(onClick: SingleClickListener): WoAlertDialog {
        onPositiveClick = onClick
        return this
    }

    class Builder(val activity: Activity) {
        internal var title: CharSequence? = null
        internal var message: CharSequence? = null
        internal var negativeText: CharSequence? = null
        internal var positiveText: CharSequence? = null
        internal var onPositiveClick: SingleClickListener? = null
        internal var onNegativeClick: SingleClickListener? = null
        internal var onDismissListener: DialogInterface.OnDismissListener? = null
        internal var cancelable: Boolean = true
        internal var autoDismiss: Boolean = true
        internal var customView: View? = null

        fun cancelable(cancelable: Boolean): Builder {
            this.cancelable = cancelable
            return this
        }

        fun canceledOnTouchOutside(cancelable: Boolean): Builder {
            this.cancelable = cancelable
            return this
        }

        fun autoDismiss(autoDismiss: Boolean): Builder {
            this.autoDismiss = autoDismiss
            return this
        }

        fun title(@StringRes id: Int): Builder {
            this.title = activity.getString(id)
            return this
        }

        fun title(text: CharSequence): Builder {
            this.title = text
            return this
        }

        fun content(@StringRes id: Int): Builder {
            return content(id, false)
        }

        fun content(@StringRes id: Int, isHtml: Boolean): Builder {
            val text = activity.getString(id)
            if (isHtml) {
                this.message = Html.fromHtml(text.replace("\n", "<br/>"))
            } else {
                this.message = text
            }
            return this
        }

        fun content(text: CharSequence): Builder {
            this.message = text
            return this
        }

        fun negativeText(@StringRes id: Int): Builder {
            this.negativeText = activity.getString(id)
            return this
        }

        fun negativeText(text: CharSequence): Builder {
            this.negativeText = text
            return this
        }

        fun positiveText(@StringRes id: Int): Builder {
            this.positiveText = activity.getString(id)
            return this
        }

        fun positiveText(text: CharSequence): Builder {
            this.positiveText = text
            return this
        }

        fun onPositive(onClick: SingleClickListener): Builder {
            this.onPositiveClick = onClick
            return this
        }

        fun onNegative(onClick: SingleClickListener): Builder {
            this.onNegativeClick = onClick
            return this
        }

        fun dismissListener(listener: DialogInterface.OnDismissListener): Builder {
            this.onDismissListener = listener
            return this
        }

        fun customView(@LayoutRes id: Int, wrapInScrollView: Boolean): Builder {
            return customView(LayoutInflater.from(activity).inflate(id, null), wrapInScrollView)
        }

        fun customView(view: View, wrapInScrollView: Boolean): Builder {
            customView = view
            return this
        }

        fun build(): WoAlertDialog {
            return WoAlertDialog(this)
        }

        fun show(): WoAlertDialog {
            val dialog = build()
            dialog.show()
            return dialog
        }
    }
}