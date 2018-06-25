package com.baryshev.currency.data.currency.db

import android.arch.persistence.room.*


@Dao
interface CurrencyDao {

    @Query("SELECT COUNT(*) FROM  currencies")
    fun count(): Int

    @Query("select * from currencies")
    fun getAllCurrencies(): List<CurrencyEntity>

    @Query("select * from currencies where id = :id")
    fun findCurrencyById(id: Long): CurrencyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrency(vararg note: CurrencyEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateCurrency(vararg note: CurrencyEntity)

    @Delete
    fun deleteCurrency(vararg note: CurrencyEntity)

    @Query("DELETE FROM currencies WHERE id = :id")
    fun deleteCurrencyById(id: Long)
}