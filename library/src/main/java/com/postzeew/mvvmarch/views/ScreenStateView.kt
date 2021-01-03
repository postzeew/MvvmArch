package com.postzeew.mvvmarch.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.Dimension.SP
import androidx.annotation.FontRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.postzeew.mvvmarch.R
import com.postzeew.mvvmarch.core.MvvmArch
import com.postzeew.mvvmarch.databinding.ViewScreenStateBinding
import com.postzeew.mvvmarch.errors.ErrorInfo
import com.postzeew.mvvmarch.extensions.gone
import com.postzeew.mvvmarch.extensions.show

class ScreenStateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding = ViewScreenStateBinding.inflate(LayoutInflater.from(context), this)

    lateinit var onRetryClickListener: (() -> Unit)

    init {
        applyConfig(MvvmArch.screenStateViewConfig)
        gone()
        setBackgroundColor(Color.WHITE)
        isClickable = true
        isFocusable = true
        translationZ = context.resources.getDimension(R.dimen.translation_z)
        binding.retryButton.setOnClickListener {
            onRetryClickListener.invoke()
        }
    }

    fun showLoading() {
        binding.loadingFrameLayout.show()
        show()
    }

    fun hideLoading() {
        binding.loadingFrameLayout.gone()
        gone()
    }

    fun showError(errorInfo: ErrorInfo) {
        with(binding) {
            errorTitleTextView.text = errorInfo.title
            errorDescriptionTextView.text = errorInfo.description
            errorLinearLayout.show()
            show()
        }
    }

    fun hideError() {
        binding.errorLinearLayout.gone()
        gone()
    }

    private fun applyConfig(config: Config) {
        config.titleConfig?.let { titleConfig ->
            applyTextConfig(binding.errorTitleTextView, titleConfig)
        }

        config.descriptionConfig?.let { descriptionConfig ->
            applyTextConfig(binding.errorDescriptionTextView, descriptionConfig)
        }

        config.buttonConfig?.let { buttonConfig ->
            applyTextConfig(binding.retryButton, buttonConfig)

            with(buttonConfig) {
                text?.let { text ->
                    binding.retryButton.text = text
                }
                backgroundColor?.let { backgroundColor ->
                    binding.retryButton.backgroundTintList = ColorStateList.valueOf(backgroundColor)
                }
            }
        }

        config.loaderConfig?.color?.let { color ->
            DrawableCompat.setTint(binding.progressBar.indeterminateDrawable, color)
        }
    }

    private fun applyTextConfig(textView: TextView, textConfig: Config.TextConfig) {
        with(textConfig) {
            fontResId?.let { fontResId ->
                textView.typeface = ResourcesCompat.getFont(context, fontResId)
            }
            textSize?.let { textSize ->
                textView.textSize = textSize
            }
            textColor?.let { textColor ->
                textView.setTextColor(textColor)
            }
            isTextBold?.let { isTextBold ->
                textView.typeface = Typeface.create(
                    textView.typeface,
                    if (isTextBold) Typeface.BOLD else Typeface.NORMAL
                )
            }
        }
    }

    data class Config(
        val titleConfig: TextConfig? = null,
        val descriptionConfig: TextConfig? = null,
        val buttonConfig: ButtonConfig? = null,
        val loaderConfig: LoaderConfig? = null

    ) {
        open class TextConfig(
            @FontRes open val fontResId: Int? = null,
            @Dimension(unit = SP) open val textSize: Float? = null,
            @ColorInt open val textColor: Int? = null,
            open val isTextBold: Boolean? = null
        )

        data class ButtonConfig(
            @FontRes override val fontResId: Int? = null,
            @Dimension(unit = SP) override val textSize: Float? = null,
            @ColorInt override val textColor: Int? = null,
            override val isTextBold: Boolean? = null,
            val text: String? = null,
            @ColorInt val backgroundColor: Int? = null
        ) : TextConfig(fontResId, textSize, textColor, isTextBold)

        data class LoaderConfig(
            @ColorInt val color: Int? = null
        )
    }
}