package com.target.supermarket.data.remote

import com.target.supermarket.domain.models.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TargetApi {
    @GET("api")
    suspend fun getAllCategories():Response<List<Category>>

    @GET("api/allProducts")
    suspend fun getAllProducts():Response<List<ServerProduct>>

    @GET("api/allProducts")
    suspend fun getAllProductsPerPage(@Query("page") page:Int):ProductResponse

    @GET("api/allSeasons")
    suspend fun getAllSeasons():Response<List<Season>>

    @GET("api/category/{id}")
    suspend fun getCategoryByID(@Path("id") id:Int):Response<Category>

    @GET("api/products/{catID}")
    suspend fun getItemsByCategory(@Path("catID") id:Int):Response<List<LocalProduct>>

    @GET("api/product/{id}")
    suspend fun getItemByID(@Path("id") id:Int):Response<ServerProduct>

    @GET("api/bannersBySeason/{SeasonID}")
    suspend fun getBannersBySeason(@Path("SeasonID") id:Int):Response<Product>

    @GET("api/banners")
    suspend fun getAllBanners():Response<List<Banner>>

    @GET("api/deals")
    suspend fun getDeals():Response<List<Product>>

    @GET("api/offers")
    suspend fun getOffers():Response<List<Product>>

    @GET("api/recomm")
    suspend fun getRecommended():Response<List<Product>>

    @GET("api/special")
    suspend fun getSpecial():Response<List<Product>>

    @POST("api/addToken")
    suspend fun addToken(@Body token: UserToken):Response<UserToken>


    @POST("api/addUser")
    suspend fun addUser(@Body customer: Customer):Response<Customer>

    @GET("api/banner/{id}")
    suspend fun getBannerById(@Path("id") id: Int): Response<Banner>

    @GET("api/season/{id}")
    suspend fun getSeason(@Path("id") id: Int): Response<Season>

    @GET("api/byTags/{tags}")
    fun getProductsByTags(@Path("tags") tags: String): Response<List<Product>>

    @GET("api/search/{query}")
    suspend fun search(@Path("query") query: String): Response<SearchResult>

    @POST("api/order")
    suspend fun order(@Body details:OrderDetails):Response<OrderDetails>

    @GET("api/check")
    suspend fun checkAPI():Response<Int>

    @POST("api/addressing")
    suspend fun getAddress(@Body spec: AddressSpec):Response<List<AddResponse>>

    @GET("api/tags")
    suspend fun getAllTags():Response<List<Tag>>

    @GET("api/district")
    suspend fun getDistricts():Response<List<District>>

    @GET("api/division")
    suspend fun getDivision():Response<List<Division>>

    @GET("api/parish")
    suspend fun getParish():Response<List<Parish>>

    @GET("api/village")
    suspend fun getVillage():Response<List<Village>>

    @GET("api/street")
    suspend fun getStreet():Response<List<Street>>
}