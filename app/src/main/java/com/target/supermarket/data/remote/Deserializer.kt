package com.target.supermarket.data.remote

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.target.supermarket.domain.ProductSource
import com.target.supermarket.domain.models.Product
import com.target.supermarket.domain.models.ProductResponse
import java.lang.reflect.Type

class Deserializer {
    private val gsonBuilder = GsonBuilder()
    private val deserializer:JsonDeserializer<ProductResponse> =
        JsonDeserializer<ProductResponse> { json, typeOfT, context ->
            val jsonObject = json.asJsonObject
            ProductResponse(
                page = jsonObject.get("next").asString.split("=")[1].toInt()-1,
                per_page = 20,
                total = jsonObject.get("count").asInt,
                total_pages = 1000,
                data = jsonObject.get("results") as List<Product>,
            )
        }
    fun deserialize(productJson:String): ProductResponse{
        gsonBuilder.registerTypeAdapter(ProductResponse::class.java,deserializer)
        val customGson = gsonBuilder.create()
        return customGson.fromJson(productJson,ProductResponse::class.java)
    }
}