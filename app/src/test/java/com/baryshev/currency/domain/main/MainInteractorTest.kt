package com.baryshev.currency.domain.main

import com.baryshev.currency.data.common.AppDb
import com.baryshev.currency.data.currency.db.CurrencyDao
import com.baryshev.currency.data.currency.db.CurrencyEntity
import com.baryshev.currency.data.exchange.db.ExchangeDao
import com.baryshev.currency.data.exchange.db.ExchangeEntity
import com.baryshev.currency.data.exchange.network.ExchangeApi
import com.baryshev.currency.data.exchange.network.pojo.ExchangeResponse
import com.baryshev.currency.data.exchange.network.pojo.Rates
import com.baryshev.currency.data.exchange.network.repo.ExchangeRepo
import com.baryshev.currency.domain.common.IResourceInteractor
import com.baryshev.currency.domain.common.exception.ExchangeException
import com.baryshev.currency.utils.extensions.formatToDateString
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class MainInteractorTest {

    @Mock
    private lateinit var resourceInteractor: IResourceInteractor
    @Mock
    private lateinit var appDb: AppDb
    @Mock
    private lateinit var currencyDao: CurrencyDao
    @Mock
    private lateinit var exchangeDao: ExchangeDao
    @Mock
    private lateinit var exchangeApi: ExchangeApi

    private var interactor: MainInteractor? = null

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(appDb.currencyDao()).thenReturn(currencyDao)
        Mockito.`when`(appDb.exchangeDao()).thenReturn(exchangeDao)

        interactor = MainInteractor(appDb, ExchangeRepo(exchangeApi), resourceInteractor)
    }

    @Test
    fun `convert without refreshing with success - succes`() {

        interactor?.exchangeKoeff = 1.0
        val testCurrency = MainData.Currency("test", "test")
        interactor?.data?.apply {
            from = testCurrency
            to = testCurrency
        }

        val test = interactor?.convert("test", "test", 1.0, false)
            ?.test()

        test?.assertNoErrors()
        test?.assertComplete()
        test?.assertValueCount(1)
        assert(test?.values()?.get(0) == MainData.Exchange(testCurrency,
                                                           testCurrency,
                                                           false,
                                                           "",
                                                           "",
                                                           1.0))
    }

    @Test
    fun `convert with refreshing and success response - success`() {

        interactor?.exchangeKoeff = 1.0
        val testCurrency = MainData.Currency("test", "test")
        interactor?.data?.apply {
            from = testCurrency
            to = testCurrency
        }

        `when`(currencyDao.findCurrencyByCc(anyString()))
            .thenReturn(CurrencyEntity("test", "test", "test"))
        `when`(exchangeApi.getConvertedExchange())
            .thenReturn(Single.just(ExchangeResponse(true,
                                                     null,
                                                     10000L,
                                                     "test",
                                                     "test",
                                                     Rates(mapOf(Pair("test", 1.0))))))


        val test = interactor?.convert("test", "test", 1.0, true)
            ?.test()

        test?.assertNoErrors()
        test?.assertComplete()
        test?.assertValueCount(1)
        assert(test?.values()?.get(0) == MainData.Exchange(testCurrency,
                                                           testCurrency,
                                                           false,
                                                           "",
                                                           "",
                                                           1.0))
        verify(exchangeDao).insertExchange(mutableListOf(ExchangeEntity("test",
                                                                        1.0,
                                                                        10000L.formatToDateString())))
    }

    @Test
    fun `convert difference currencies with refreshing and success response - success`() {

        interactor?.exchangeKoeff = 1.0
        val testCurrency = MainData.Currency("test", "test")
        interactor?.data?.apply {
            from = testCurrency
            to = testCurrency
        }

        `when`(currencyDao.findCurrencyByCc("test1"))
            .thenReturn(CurrencyEntity("test1", "test1", "test1"))
        `when`(currencyDao.findCurrencyByCc("test2"))
            .thenReturn(CurrencyEntity("test2", "test2", "test2"))
        `when`(exchangeApi.getConvertedExchange())
            .thenReturn(Single.just(ExchangeResponse(true,
                                                     null,
                                                     10000L,
                                                     "test",
                                                     "test",
                                                     Rates(mapOf(Pair("test1", 2.0),
                                                                 Pair("test2", 10.0))))))


        val test = interactor?.convert("test1", "test2", 1.0, true)
            ?.test()

        test?.assertNoErrors()
        test?.assertComplete()
        test?.assertValueCount(1)
        assert(test?.values()?.get(0) == MainData.Exchange(MainData.Currency("test1", "test1"),
                                                           MainData.Currency("test2", "test2"),
                                                           false,
                                                           "",
                                                           "",
                                                           5.0))
    }

    @Test
    fun `convert with refreshing and failed response - getting data from database`() {

        interactor?.exchangeKoeff = 1.0
        val testCurrency = MainData.Currency("test", "test")
        interactor?.data?.apply {
            from = testCurrency
            to = testCurrency
        }

        `when`(currencyDao.findCurrencyByCc(anyString()))
            .thenReturn(CurrencyEntity("test", "test", "test"))
        `when`(exchangeDao.findExchangeByName(anyString()))
            .thenReturn(ExchangeEntity("test", 1.0, "date"))
        `when`(exchangeApi.getConvertedExchange())
            .thenReturn(Single.just(ExchangeResponse(false,
                                                     com.baryshev.currency.data.exchange.network.pojo.Error(
                                                             100,
                                                             "error"),
                                                     10000L,
                                                     "test",
                                                     "test",
                                                     Rates(mapOf(Pair("test", 1.0))))))


        val test = interactor?.convert("test", "test", 1.0, true)?.test()

        test?.assertNoErrors()
        test?.assertComplete()
        test?.assertValueCount(1)
        assert(test?.values()?.get(0) == MainData.Exchange(testCurrency,
                                                           testCurrency,
                                                           true,
                                                           "date",
                                                           "error",
                                                           1.0))
        verify(exchangeDao, never()).insertExchange(mutableListOf(ExchangeEntity("test",
                                                                                 1.0,
                                                                                 10000L.formatToDateString())))
    }

    @Test
    fun `convert with refreshing and failed response and data from db is invalid - error`() {

        interactor?.exchangeKoeff = 1.0
        val testCurrency = MainData.Currency("test", "test")
        interactor?.data?.apply {
            from = testCurrency
            to = testCurrency
        }

        `when`(currencyDao.findCurrencyByCc(anyString()))
            .thenReturn(CurrencyEntity("test", "test", "test"))
        `when`(exchangeDao.findExchangeByName(anyString()))
            .thenReturn(ExchangeEntity("test",  -1.0, "date"))
        `when`(exchangeApi.getConvertedExchange())
            .thenReturn(Single.just(ExchangeResponse(false,
                                                     com.baryshev.currency.data.exchange.network.pojo.Error(
                                                             100,
                                                             "error"),
                                                     10000L,
                                                     "test",
                                                     "test",
                                                     Rates(mapOf(Pair("test", 1.0))))))


        val test = interactor?.convert("test", "test", 1.0, true)?.test()

        test?.assertError(ExchangeException::class.java)
        test?.assertNoValues()
        test?.assertNotComplete()
    }

    @Test
    fun `convert with refreshing and failed response and data from db have different dates - error`() {

        interactor?.exchangeKoeff = 1.0
        val testCurrency = MainData.Currency("test", "test")
        interactor?.data?.apply {
            from = testCurrency
            to = testCurrency
        }

        `when`(currencyDao.findCurrencyByCc("test1"))
            .thenReturn(CurrencyEntity("test1", "test1", "test1"))
        `when`(currencyDao.findCurrencyByCc("test2"))
            .thenReturn(CurrencyEntity("test2", "test2", "test2"))
        `when`(exchangeDao.findExchangeByName("test1"))
            .thenReturn(ExchangeEntity("test1",  1.0, "date1"))
        `when`(exchangeDao.findExchangeByName("test2"))
            .thenReturn(ExchangeEntity("test2",  1.0, "date2"))
        `when`(exchangeApi.getConvertedExchange())
            .thenReturn(Single.just(ExchangeResponse(false,
                                                     com.baryshev.currency.data.exchange.network.pojo.Error(
                                                             100,
                                                             "error"),
                                                     10000L,
                                                     "test",
                                                     "test",
                                                     Rates(mapOf(Pair("test", 1.0))))))


        val test = interactor?.convert("test1", "test2", 1.0, true)?.test()

        test?.assertError(ExchangeException::class.java)
        test?.assertNoValues()
        test?.assertNotComplete()
    }
}