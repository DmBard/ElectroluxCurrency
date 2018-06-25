package com.baryshev.currency.presentation.main.vm

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.baryshev.currency.TestRxScheduler
import com.baryshev.currency.domain.common.exception.ExchangeException
import com.baryshev.currency.domain.main.MainData
import com.baryshev.currency.domain.main.MainInteractor
import com.baryshev.currency.presentation.main.view.MainViewResult
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class MainVMTest {
    @Mock
    private lateinit var mainObserver: Observer<MainViewResult>
    @Mock
    private lateinit var validationObserver: Observer<Boolean>
    @Mock
    private lateinit var mainInteractor: MainInteractor

    private var viewModel: MainVM? = null

    //    the hack field to test LiveData
    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = MainVM(TestRxScheduler(), mainInteractor)
        viewModel?.mainData?.observeForever(mainObserver)
        viewModel?.amountValidation?.observeForever(validationObserver)
    }

    @Test
    fun `convert with success`() {
        val testCurrency = MainData.Currency("test", "test")

        val testExchange = MainData.Exchange(testCurrency,
                                             testCurrency,
                                             true,
                                             "test",
                                             "error",
                                             1.0
                                            )
        `when`(mainInteractor.convert(anyString(),
                                      anyString(),
                                      anyDouble(),
                                      anyBoolean())).thenReturn(Single.just(testExchange))

        viewModel?.convert("test", "test", "1.0", true)

        verify(validationObserver).onChanged(true)
        verify(mainObserver).onChanged(MainViewResult.Progress)
        verify(mainObserver).onChanged(MainViewResult.Success(testExchange))
        verify(mainObserver, never()).onChanged(Mockito.any(MainViewResult.Error::class.java))
    }

    @Test
    fun `convert with error`() {
        val exception = ExchangeException("error")
        `when`(mainInteractor.convert(anyString(),
                                      anyString(),
                                      anyDouble(),
                                      anyBoolean())).thenReturn(Single.error(exception))

        viewModel?.convert("test", "test", "1.0", true)

        verify(validationObserver).onChanged(true)
        verify(mainObserver).onChanged(MainViewResult.Progress)
        verify(mainObserver, never()).onChanged(Mockito.any(MainViewResult.Success::class.java))
        verify(mainObserver).onChanged(MainViewResult.Error("error", exception))
    }

    @Test
    fun `convert without refreshing`() {
        val testCurrency = MainData.Currency("test", "test")

        val testExchange = MainData.Exchange(testCurrency,
                                             testCurrency,
                                             true,
                                             "test",
                                             "error",
                                             1.0
                                            )
        `when`(mainInteractor.convert(anyString(),
                                      anyString(),
                                      anyDouble(),
                                      anyBoolean())).thenReturn(Single.just(testExchange))

        viewModel?.convert("test", "test", "1.0", false)

        verify(mainObserver, never()).onChanged(MainViewResult.Progress)
    }

    @Test
    fun `convert with invalid amount`() {
        val testCurrency = MainData.Currency("test", "test")

        val testExchange = MainData.Exchange(testCurrency,
                                             testCurrency,
                                             true,
                                             "test",
                                             "error",
                                             1.0
                                            )
        `when`(mainInteractor.convert(anyString(),
                                      anyString(),
                                      anyDouble(),
                                      anyBoolean())).thenReturn(Single.just(testExchange))

        viewModel?.convert("test", "test", "invalid AMOUNT", false)

        verify(validationObserver).onChanged(false)
    }
}