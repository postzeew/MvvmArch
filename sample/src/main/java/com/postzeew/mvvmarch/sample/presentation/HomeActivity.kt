package com.postzeew.mvvmarch.sample.presentation

import android.os.Bundle
import androidx.lifecycle.Observer
import com.postzeew.mvvmarch.core.presentation.ViewModelActivity
import com.postzeew.mvvmarch.sample.databinding.ActivityHomeBinding

class HomeActivity : ViewModelActivity<HomeViewModel>(HomeViewModelImpl::class.java) {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.getTimeButton.setOnClickListener {
            viewModel.onGetTimeButtonClicked()
        }
    }

    override fun subscribeToViewModel() {
        super.subscribeToViewModel()
        viewModel.time.observe(this, Observer { time ->
            binding.timeTextView.text = time
        })
    }
}