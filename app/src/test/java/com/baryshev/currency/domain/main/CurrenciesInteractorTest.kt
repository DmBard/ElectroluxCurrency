package com.baryshev.currency.domain.main

import com.baryshev.currency.data.common.AppDb
import com.baryshev.currency.data.currency.db.CurrencyDao
import com.baryshev.currency.data.currency.db.CurrencyEntity
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class CurrenciesInteractorTest {
    @Mock
    private lateinit var appDb: AppDb
    @Mock
    private lateinit var currencyDao: CurrencyDao
    private var interactor: CurrenciesInteractor? = null

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        `when`(appDb.currencyDao()).thenReturn(currencyDao)

        interactor = CurrenciesInteractor(appDb)

    }

    @Test
    fun `getCurrencies with success`() {

        `when`(currencyDao.getAllCurrencies()).thenReturn(listOf(CurrencyEntity("test",
                                                                                "test",
                                                                                "test"),
                                                                 CurrencyEntity("test",
                                                                                "test",
                                                                                "test")))


        val test = interactor?.getCurrencies()?.test()

        test?.assertNoErrors()
        test?.assertComplete()
        test?.assertValueCount(1)
        assertEquals(test?.valueCount(), 1)
        val list = test?.values()?.get(0)!!
        assert(list.isNotEmpty())
        assert(list.size == 2)
        assert(list.contains(MainData.Currency("test", "test")))
        assert(list[0] == MainData.Currency("test", "test"))
        assert(list[1] == MainData.Currency("test", "test"))
    }

    @Test
    fun `getCurrencies returns empty list`() {

        `when`(currencyDao.getAllCurrencies()).thenReturn(emptyList())


        val test = interactor?.getCurrencies()?.test()

        test?.assertNoErrors()
        test?.assertComplete()
        test?.assertValueCount(1)
        assertEquals(test?.valueCount(), 1)
        val list = test?.values()?.get(0)!!
        assert(list.isEmpty())
    }

    @Test
    fun `getCurrencies returns exception`() {

        val illegalStateException = IllegalStateException()
        `when`(currencyDao.getAllCurrencies()).thenThrow(illegalStateException)


        val test = interactor?.getCurrencies()?.test()

        test?.assertError(illegalStateException)
        test?.assertValueCount(0)
    }
}