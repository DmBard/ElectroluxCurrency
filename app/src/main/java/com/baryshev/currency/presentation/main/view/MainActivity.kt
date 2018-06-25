package com.baryshev.currency.presentation.main.view

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import com.baryshev.currency.R
import com.baryshev.currency.app
import com.baryshev.currency.domain.main.MainData
import com.baryshev.currency.presentation.main.vm.MainVM
import com.baryshev.currency.utils.extensions.makeErrorSnackBar
import com.baryshev.currency.utils.extensions.observe
import com.baryshev.currency.utils.extensions.withViewModel
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

const val FROM_DEFAULT = "SEK"
const val TO_DEFAULT = "USD"
const val AMOUNT_DEFAULT = 1.0

class MainActivity : AppCompatActivity(), CurrenciesDialogFragment.OnCurrencyClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        withViewModel<MainVM>(viewModelFactory) {
            observe(mainData) {
                handleResult(it)
            }

            observe(amountValidation) {
                if (it == false) {
                    snackbar = makeErrorSnackBar(mainLayout,
                                                 null,
                                                 { etFromAmount.setText(AMOUNT_DEFAULT.toString()) },
                                                 getString(R.string.invalid_amount),
                                                 getString(R.string.clear)).apply { show() }
                }
            }
        }
    }

    private var snackbar: Snackbar? = null

    private var textWatcher: TextWatcher? = null

    private fun initDagger() {
        app.injector?.initMainComponent()?.inject(this)
    }

    override fun onStop() {
        super.onStop()
        textWatcher?.let { etFromAmount.removeTextChangedListener(it) }
        if (!isChangingConfigurations) app.injector?.clearMainComponent()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initDagger()

        tvFromTitle.setOnClickListener {
            CurrenciesDialogFragment().apply {
                arguments = Bundle().apply {
                    putInt(CurrenciesDialogFragment.CURRENCY_DIRECTION_TYPE, FROM)
                }
                show(supportFragmentManager, "")
            }
        }
        tvToTitle.setOnClickListener {
            CurrenciesDialogFragment().apply {
                arguments = Bundle().apply {
                    putInt(CurrenciesDialogFragment.CURRENCY_DIRECTION_TYPE, TO)
                }
                show(supportFragmentManager, "")
            }
        }

        fabExchange.setOnClickListener({ convert() })

        textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    etFromAmount.removeTextChangedListener(this)
                    etFromAmount.setText(AMOUNT_DEFAULT.toString())
                    etFromAmount.addTextChangedListener(this)
                }

                convert(false)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        }
        etFromAmount.addTextChangedListener(textWatcher)

        ivReplace.setOnClickListener {
            val fromCc = tvFromTitle.text
            val toCc = tvToTitle.text
            val fromName = tvFromName.text
            val toName = tvToName.text

            tvFromTitle.text = toCc
            tvToTitle.text = fromCc
            tvFromName.text = toName
            tvToName.text = fromName

            convert()
        }

        if (savedInstanceState == null) {
            convert()
        }
    }

    private fun convert(needToRefresh: Boolean = true) {
        viewModel.convert(tvFromTitle.text.toString(),
                          tvToTitle.text.toString(),
                          etFromAmount.text.toString(),
                          needToRefresh)
    }

    private fun handleResult(result: MainViewResult?) {
        when (result) {
            is MainViewResult.Progress -> {
                handleProgress()
            }
            is MainViewResult.Success -> {
                handleSuccess(result)
            }
            is MainViewResult.Error -> {
                handleError(result)
            }
        }
    }

    private fun handleError(result: MainViewResult.Error) {
        pbExchangeLoading.post { pbExchangeLoading.visibility = INVISIBLE }
        ivReplace.visibility = VISIBLE
        snackbar = makeErrorSnackBar(mainLayout,
                                     result.error,
                                     { convert() },
                                     result.message).apply { show() }
    }

    private fun handleSuccess(result: MainViewResult.Success) {
        pbExchangeLoading.post { pbExchangeLoading.visibility = INVISIBLE }
        ivReplace.visibility = VISIBLE

        with(result.data) {
            val from = this.from
            if (from != null) {
                tvFromTitle.text = from.cc
                tvFromName.text = from.name
            }

            val to = this.to
            if (to != null) {
                tvToTitle.text = to.cc
                tvToName.text = to.name
            }

            tvToAmount.text = this.result.toString()
            if (this.isCache) {
                tvUpdatingDate.visibility = VISIBLE
                tvUpdatingDate.text = """${getString(R.string.last_update)}${this.cacheDate}"""
            } else {
                tvUpdatingDate.visibility = INVISIBLE
            }

            val errorMessage = this.error
            if (!errorMessage.isEmpty()) {
                snackbar = makeErrorSnackBar(mainLayout,
                                             null,
                                             { convert() },
                                             errorMessage).apply { show() }
            }
        }
    }

    private fun handleProgress() {
        snackbar?.dismiss()
        pbExchangeLoading.post { pbExchangeLoading.visibility = VISIBLE }
        ivReplace.visibility = INVISIBLE
    }

    override fun onChooseCurrency(currency: MainData.Currency, @CurrencyDirectionType direction: Int) {
        when (direction) {
            FROM -> {
                tvFromTitle.text = currency.cc
                tvFromName.text = currency.name
            }
            TO -> {
                tvToTitle.text = currency.cc
                tvToName.text = currency.name
            }
        }
        convert()
    }
}
