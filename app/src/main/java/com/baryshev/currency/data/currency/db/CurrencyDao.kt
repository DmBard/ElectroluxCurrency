package com.baryshev.currency.data.currency.db

import android.arch.persistence.room.*


@Dao
interface CurrencyDao {

    @Query("SELECT COUNT(*) FROM  currencies")
    fun count(): Int

    @Query("select * from currencies")
    fun getAllCurrencies(): List<CurrencyEntity>

    @Query("select * from currencies where cc = :name")
    fun findCurrencyByCc(name: String): CurrencyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrency(vararg note: CurrencyEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateCurrency(vararg note: CurrencyEntity)

    @Delete
    fun deleteCurrency(vararg note: CurrencyEntity)
}