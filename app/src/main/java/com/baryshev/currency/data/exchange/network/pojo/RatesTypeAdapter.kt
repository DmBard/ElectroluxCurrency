package com.baryshev.currency.data.exchange.network.pojo

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter


class RatesTypeAdapter : TypeAdapter<Rates>() {
    override fun write(out: JsonWriter?, value: Rates?) {
    }

    override fun read(inj: JsonReader?): Rates {
        val gson = Gson()
        val jsonObject: JsonObject = gson.fromJson(inj, object : TypeToken<JsonObject>() {}.type)
        val hashMapType = object : TypeToken<HashMap<String, Double>>() {}.type
        val values: Map<String, Double> = gson.fromJson(jsonObject, hashMapType)
        return Rates(values)
    }
}