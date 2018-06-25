package com.baryshev.currency.presentation.main.view

import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.baryshev.currency.R
import com.baryshev.currency.app
import com.baryshev.currency.domain.main.MainData
import com.baryshev.currency.presentation.main.vm.CurrenciesVM
import com.baryshev.currency.utils.extensions.observe
import com.baryshev.currency.utils.extensions.withViewModel
import javax.inject.Inject

class CurrenciesDialogFragment : BottomSheetDialogFragment() {

    companion object {
        const val CURRENCY_DIRECTION_TYPE = "CURRENCY_DIRECTION_TYPE"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        withViewModel<CurrenciesVM>(viewModelFactory) {
            observe(data) {
                handleResult(it)
            }
        }
    }

    private var currencyClickListener: OnCurrencyClickListener? = null

    private val mainAdapter: CurrenciesAdapter by lazy {
        CurrenciesAdapter(object : CurrenciesAdapter.ClickListener {
            override fun onClick(pos: Int) {
                @CurrencyDirectionType val direction = arguments?.getInt(CURRENCY_DIRECTION_TYPE,
                                                                         UNDEFINED) ?: UNDEFINED
                currencyClickListener?.onChooseCurrency(mainAdapter.getCurrencyByPosition(pos),
                                                        direction)
                dismiss()
            }
        })
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnCurrencyClickListener) {
            currencyClickListener = context
        } else {
            throw IllegalStateException("Activity must implement OnCurrencyClickListener")
        }
    }


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dialog_currencies, container, false)

        val linearLayoutManager = LinearLayoutManager(activity)
        val rvCurrencies: RecyclerView = view.findViewById(R.id.rvCurrencies)

        with(rvCurrencies) {
            layoutManager = linearLayoutManager
            setHasFixedSize(false)
            adapter = mainAdapter
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDagger()

        viewModel.init()
    }

    private fun initDagger() {
        activity?.app?.injector?.initCurrenciesComponent()?.inject(this)
    }

    override fun onStop() {
        super.onStop()
        if (activity?.isChangingConfigurations == false) activity?.app?.injector?.clearCurrenciesComponent()
    }

    private fun handleResult(result: CurrenciesViewResult?) {

        view?.let {
            val pbLoading: ProgressBar = it.findViewById(R.id.pbLoading)
            val tvError: TextView = it.findViewById(R.id.tvError)

            when (result) {
                is CurrenciesViewResult.Success -> {
                    pbLoading.visibility = GONE
                    tvError.visibility = GONE
                    mainAdapter.submitList(result.data)
                }
                is CurrenciesViewResult.Error -> {
                    pbLoading.visibility = GONE
                    tvError.visibility = VISIBLE
                    tvError.text = result.message
                }
                is CurrenciesViewResult.Progress -> {
                    pbLoading.visibility = VISIBLE
                    tvError.visibility = GONE
                }
            }
        }

    }

    interface OnCurrencyClickListener {
        fun onChooseCurrency(currency: MainData.Currency,
                             @CurrencyDirectionType direction: Int)
    }
}