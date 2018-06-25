package com.baryshev.currency.data.exchange.db

import android.arch.persistence.room.*

@Dao
interface ExchangeDao {

    @Query("SELECT COUNT(*) FROM exchanges")
    fun count(): Int

    @Query("select * from exchanges")
    fun getAllExcangies(): List<ExchangeEntity>

    @Query("select * from exchanges where id = :id")
    fun findExchangeById(id: Long): ExchangeEntity?

    @Query("select * from exchanges where (`from` = :fromId and `to` = :toId)")
    fun findExchangeByCurrencies(fromId: String, toId: String): ExchangeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExchange(vararg note: ExchangeEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateExchange(vararg note: ExchangeEntity)

    @Delete
    fun deleteExchange(vararg note: ExchangeEntity)

    @Query("DELETE FROM exchanges WHERE id = :id")
    fun deleteExchangeById(id: Long)
}