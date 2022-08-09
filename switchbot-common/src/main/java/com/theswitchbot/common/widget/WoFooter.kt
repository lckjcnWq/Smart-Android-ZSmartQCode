package com.theswitchbot.common.widget

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import com.scwang.smartrefresh.layout.api.RefreshFooter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.internal.ArrowDrawable
import com.scwang.smartrefresh.layout.internal.InternalClassics
import com.scwang.smartrefresh.layout.internal.ProgressDrawable
import com.scwang.smartrefresh.layout.util.SmartUtil
import com.theswitchbot.common.R

/**
 * 经典上拉底部组件
 * Created by scwang on 2017/5/28.
 */
class WoFooter @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    InternalClassics<ClassicsFooter?>(context, attrs, 0), RefreshFooter {
    protected var mTextPulling //"上拉加载更多";
            : String? = null
    protected var mTextRelease //"释放立即加载";
            : String? = null
    protected var mTextLoading //"正在加载...";
            : String? = null
    protected var mTextRefreshing //"正在刷新...";
            : String? = null
    protected var mTextFinish //"加载完成";
            : String? = null
    protected var mTextFailed //"加载失败";
            : String? = null
    protected var mTextNothing //"没有更多数据了";
            : String? = null
    protected var mNoMoreData = false

    //</editor-fold>
    //<editor-fold desc="RefreshFooter">
    override fun onStartAnimator(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
        if (!mNoMoreData) {
            super.onStartAnimator(refreshLayout, height, maxDragHeight)
        }
    }

    override fun onFinish(layout: RefreshLayout, success: Boolean): Int {
        if (!mNoMoreData) {
            mTitleText.text = if (success) mTextFinish else mTextFailed
            return super.onFinish(layout, success)
        }
        return 0
    }

    /**
     * ClassicsFooter 在(SpinnerStyle.FixedBehind)时才有主题色
     */
    @Deprecated("")
    override fun setPrimaryColors(@ColorInt vararg colors: Int) {
        if (mSpinnerStyle === SpinnerStyle.FixedBehind) {
            super.setPrimaryColors(*colors)
        }
    }

    /**
     * 设置数据全部加载完成，将不能再次触发加载功能
     */
    override fun setNoMoreData(noMoreData: Boolean): Boolean {
        if (mNoMoreData != noMoreData) {
            mNoMoreData = noMoreData
            val arrowView: View = mArrowView
            if (noMoreData) {
                mTitleText.text = mTextNothing
                arrowView.visibility = GONE
            } else {
                mTitleText.text = mTextPulling
                arrowView.visibility = VISIBLE
            }
        }
        return true
    }

    override fun onStateChanged(
        refreshLayout: RefreshLayout,
        oldState: RefreshState,
        newState: RefreshState
    ) {
        val arrowView: View = mArrowView
        if (!mNoMoreData) {
            when (newState) {
                RefreshState.None -> {
                    arrowView.visibility = VISIBLE
                    mTitleText.text = mTextPulling
                    arrowView.animate().rotation(180f)
                }
                RefreshState.PullUpToLoad -> {
                    mTitleText.text = mTextPulling
                    arrowView.animate().rotation(180f)
                }
                RefreshState.Loading, RefreshState.LoadReleased -> {
                    arrowView.visibility = GONE
                    mTitleText.text = mTextLoading
                }
                RefreshState.ReleaseToLoad -> {
                    mTitleText.text = mTextRelease
                    arrowView.animate().rotation(0f)
                }
                RefreshState.Refreshing -> {
                    mTitleText.text = mTextRefreshing
                    arrowView.visibility = GONE
                }
            }
        }
    } //</editor-fold>

    companion object {
        var REFRESH_FOOTER_PULLING: String? = null //"上拉加载更多";
        var REFRESH_FOOTER_RELEASE: String? = null //"释放立即加载";
        var REFRESH_FOOTER_LOADING: String? = null //"正在加载...";
        var REFRESH_FOOTER_REFRESHING: String? = null //"正在刷新...";
        var REFRESH_FOOTER_FINISH: String? = null //"加载完成";
        var REFRESH_FOOTER_FAILED: String? = null //"加载失败";
        var REFRESH_FOOTER_NOTHING: String? = null //"没有更多数据了";
    }

    //<editor-fold desc="LinearLayout">
    init {
        inflate(context, R.layout.view_wo_footer, this)
        val thisView: View = this
        mArrowView = thisView.findViewById(R.id.srl_classics_arrow)
        val arrowView: View = mArrowView
        mProgressView = thisView.findViewById(R.id.srl_classics_progress)
        val progressView: View = mProgressView
        mTitleText = thisView.findViewById(R.id.srl_classics_title)
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ClassicsFooter)
        val lpArrow = arrowView.layoutParams as LinearLayout.LayoutParams
        val lpProgress = progressView.layoutParams as LinearLayout.LayoutParams
        lpProgress.rightMargin = ta.getDimensionPixelSize(
            R.styleable.ClassicsFooter_srlDrawableMarginRight,
            SmartUtil.dp2px(16f)
        )
        lpArrow.rightMargin = lpProgress.rightMargin
        lpArrow.width =
            ta.getLayoutDimension(R.styleable.ClassicsFooter_srlDrawableArrowSize, lpArrow.width)
        lpArrow.height =
            ta.getLayoutDimension(R.styleable.ClassicsFooter_srlDrawableArrowSize, lpArrow.height)
        lpProgress.width = ta.getLayoutDimension(
            R.styleable.ClassicsFooter_srlDrawableProgressSize,
            lpProgress.width
        )
        lpProgress.height = ta.getLayoutDimension(
            R.styleable.ClassicsFooter_srlDrawableProgressSize,
            lpProgress.height
        )
        lpArrow.width =
            ta.getLayoutDimension(R.styleable.ClassicsFooter_srlDrawableSize, lpArrow.width)
        lpArrow.height =
            ta.getLayoutDimension(R.styleable.ClassicsFooter_srlDrawableSize, lpArrow.height)
        lpProgress.width =
            ta.getLayoutDimension(R.styleable.ClassicsFooter_srlDrawableSize, lpProgress.width)
        lpProgress.height =
            ta.getLayoutDimension(R.styleable.ClassicsFooter_srlDrawableSize, lpProgress.height)
        mFinishDuration = ta.getInt(R.styleable.ClassicsFooter_srlFinishDuration, mFinishDuration)
        mSpinnerStyle = SpinnerStyle.values[ta.getInt(
            R.styleable.ClassicsFooter_srlClassicsSpinnerStyle,
            mSpinnerStyle.ordinal
        )]
        if (ta.hasValue(R.styleable.ClassicsFooter_srlDrawableArrow)) {
            mArrowView.setImageDrawable(ta.getDrawable(R.styleable.ClassicsFooter_srlDrawableArrow))
        } else if (mArrowView.drawable == null) {
            mArrowDrawable = ArrowDrawable()
            mArrowDrawable.setColor(-0x99999a)
            mArrowView.setImageDrawable(mArrowDrawable)
        }
        if (ta.hasValue(R.styleable.ClassicsFooter_srlDrawableProgress)) {
            mProgressView.setImageDrawable(ta.getDrawable(R.styleable.ClassicsFooter_srlDrawableProgress))
        } else if (mProgressView.drawable == null) {
            mProgressDrawable = ProgressDrawable()
            mProgressDrawable.setColor(-0x99999a)
            mProgressView.setImageDrawable(mProgressDrawable)
        }
        if (ta.hasValue(R.styleable.ClassicsFooter_srlTextSizeTitle)) {
            mTitleText.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                ta.getDimensionPixelSize(
                    R.styleable.ClassicsFooter_srlTextSizeTitle,
                    SmartUtil.dp2px(12f)
                ).toFloat()
            )
        }
        if (ta.hasValue(R.styleable.ClassicsFooter_srlPrimaryColor)) {
            super.setPrimaryColor(ta.getColor(R.styleable.ClassicsFooter_srlPrimaryColor, 0))
        }
        if (ta.hasValue(R.styleable.ClassicsFooter_srlAccentColor)) {
            super.setAccentColor(ta.getColor(R.styleable.ClassicsFooter_srlAccentColor, 0))
        }
        mTextPulling = if (ta.hasValue(R.styleable.ClassicsFooter_srlTextPulling)) {
            ta.getString(R.styleable.ClassicsFooter_srlTextPulling)
        } else if (REFRESH_FOOTER_PULLING != null) {
            REFRESH_FOOTER_PULLING
        } else {
            context.getString(R.string.srl_footer_pulling)
        }
        mTextRelease = if (ta.hasValue(R.styleable.ClassicsFooter_srlTextRelease)) {
            ta.getString(R.styleable.ClassicsFooter_srlTextRelease)
        } else if (REFRESH_FOOTER_RELEASE != null) {
            REFRESH_FOOTER_RELEASE
        } else {
            context.getString(R.string.srl_footer_release)
        }
        mTextLoading =
            if (ta.hasValue(R.styleable.ClassicsFooter_srlTextLoading)) {
                ta.getString(R.styleable.ClassicsFooter_srlTextLoading)
            } else if (REFRESH_FOOTER_LOADING != null) {
                REFRESH_FOOTER_LOADING
            } else {
                context.getString(R.string.srl_footer_loading)
            }
        mTextRefreshing =
            if (ta.hasValue(R.styleable.ClassicsFooter_srlTextRefreshing)) {
                ta.getString(R.styleable.ClassicsFooter_srlTextRefreshing)
            } else if (REFRESH_FOOTER_REFRESHING != null) {
                REFRESH_FOOTER_REFRESHING
            } else {
                context.getString(R.string.srl_footer_refreshing)
            }
        mTextFinish = if (ta.hasValue(R.styleable.ClassicsFooter_srlTextFinish)) {
            ta.getString(R.styleable.ClassicsFooter_srlTextFinish)
        } else if (REFRESH_FOOTER_FINISH != null) {
            REFRESH_FOOTER_FINISH
        } else {
            context.getString(R.string.srl_footer_finish)
        }
        mTextFailed = if (ta.hasValue(R.styleable.ClassicsFooter_srlTextFailed)) {
            ta.getString(R.styleable.ClassicsFooter_srlTextFailed)
        } else if (REFRESH_FOOTER_FAILED != null) {
            REFRESH_FOOTER_FAILED
        } else {
            context.getString(R.string.srl_footer_failed)
        }
        mTextNothing = if (ta.hasValue(R.styleable.ClassicsFooter_srlTextNothing)) {
            ta.getString(R.styleable.ClassicsFooter_srlTextNothing)
        } else if (REFRESH_FOOTER_NOTHING != null) {
            REFRESH_FOOTER_NOTHING
        } else {
            context.getString(R.string.srl_footer_nothing)
        }
        ta.recycle()
        progressView.animate().interpolator = null
        mTitleText.text = if (thisView.isInEditMode) mTextLoading else mTextPulling
        if (thisView.isInEditMode) {
            arrowView.visibility = GONE
        } else {
            progressView.visibility = GONE
        }
    }
}