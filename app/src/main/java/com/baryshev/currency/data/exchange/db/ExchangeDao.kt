package com.baryshev.currency.data.exchange.db

import android.arch.persistence.room.*

@Dao
interface ExchangeDao {

    @Query("SELECT COUNT(*) FROM exchanges")
    fun count(): Int

    @Query("select * from exchanges")
    fun getAllExchangies(): List<ExchangeEntity>

    @Query("select * from exchanges where name = :name")
    fun findExchangeByName(name: String): ExchangeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExchange(vararg note: ExchangeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExchange(note: List<ExchangeEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateExchange(vararg note: ExchangeEntity)

    @Delete
    fun deleteExchange(vararg note: ExchangeEntity)
}