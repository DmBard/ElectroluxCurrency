package com.baryshev.currency.presentation.launch.view

import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.baryshev.currency.R
import com.baryshev.currency.app
import com.baryshev.currency.presentation.common.SimpleViewResult
import com.baryshev.currency.presentation.launch.vm.LaunchVM
import com.baryshev.currency.presentation.main.view.MainActivity
import com.baryshev.currency.utils.extensions.makeErrorSnackBar
import com.baryshev.currency.utils.extensions.observe
import com.baryshev.currency.utils.extensions.withViewModel
import kotlinx.android.synthetic.main.activity_launch.*
import javax.inject.Inject

class LaunchActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        withViewModel<LaunchVM>(viewModelFactory) {
            observe(mainData) {
                handleResult(it)
            }
        }
    }

    private fun handleResult(simpleViewResult: SimpleViewResult?) {
        when (simpleViewResult) {
            is SimpleViewResult.Success -> startActivity(Intent(this@LaunchActivity,
                                                                MainActivity::class.java))
            is SimpleViewResult.Error -> {
                makeErrorSnackBar(launchLayout,
                                  simpleViewResult.error,
                                  {
                                      viewModel.init()
                                  },
                                  simpleViewResult.message).apply { show() }

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        initDagger()

        viewModel.init()
    }

    private fun initDagger() {
        app.injector?.initLaunchComponent()?.inject(this)
    }

    override fun onStop() {
        super.onStop()
        if (!isChangingConfigurations) app.injector?.clearLaunchComponent()
    }
}