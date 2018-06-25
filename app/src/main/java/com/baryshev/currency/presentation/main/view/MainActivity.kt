package com.baryshev.currency.presentation.main.view

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.baryshev.currency.R
import com.baryshev.currency.app
import com.baryshev.currency.domain.main.MainData
import com.baryshev.currency.presentation.main.vm.MainVM
import com.baryshev.currency.utils.extensions.observe
import com.baryshev.currency.utils.extensions.withViewModel
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), CurrenciesDialogFragment.OnCurrencyClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        withViewModel<MainVM>(viewModelFactory) {
            observe(mainData) {
                handleResult(it)
            }
        }
    }

    private fun initDagger() {
        app.injector?.initMainComponent()?.inject(this)
    }

    override fun onStop() {
        super.onStop()
        if (!isChangingConfigurations) app.injector?.clearMainComponent()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initDagger()

        viewModel.init(tvFromTitle.text.toString(),
                       tvToTitle.text.toString(),
                       etFromAmount.text.toString())
        fabExchange.setOnClickListener {
            val bottomSheetDialog = CurrenciesDialogFragment().apply {
                arguments = Bundle().apply {
                    putInt(CurrenciesDialogFragment.CURRENCY_DIRECTION_TYPE, FROM)
                }
            }

            bottomSheetDialog.show(supportFragmentManager, "Custom Bottom Sheet")
        }
    }

    private fun handleResult(result: MainViewResult?) {
        when (result) {
            is MainViewResult.Success -> {
                tvFromTitle.text= result.data.from
                tvToTitle.text= result.data.to
            }
        }
    }

    override fun onChooseCurrency(currency: MainData.Currency, direction: Int) {
    }
}
