package com.postzeew.mvvmarch.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.postzeew.mvvmarch.R
import com.postzeew.mvvmarch.core.MvvmArch
import com.postzeew.mvvmarch.databinding.ViewOverScreenLoaderBinding
import com.postzeew.mvvmarch.extensions.gone

class OverScreenLoaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding = ViewOverScreenLoaderBinding.inflate(LayoutInflater.from(context), this)

    init {
        MvvmArch.overScreenLoaderViewConfig.let(::applyConfig)
        gone()
        setBackgroundColor(ContextCompat.getColor(context, R.color.translucent))
        isClickable = true
        isFocusable = true
        translationZ = context.resources.getDimension(R.dimen.translation_z)
    }

    private fun applyConfig(config: Config) {
        config.color?.let { color ->
            DrawableCompat.setTint(binding.progressBar.indeterminateDrawable, color)
        }
    }

    data class Config(
        @ColorInt val color: Int? = null
    )
}