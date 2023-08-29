package com.target.supermarket.data.repositories

import android.util.Log
import com.target.supermarket.data.local.UserPreference
import com.target.supermarket.domain.models.CartItems
import com.target.supermarket.data.local.room.TargetDB
import com.target.supermarket.data.remote.TargetApi
import com.target.supermarket.domain.models.Address
import com.target.supermarket.domain.models.*
import com.target.supermarket.domain.repository.MainRepo
import com.target.supermarket.utilities.Constants
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.Collections.emptyList

class MainRepoImpl(private val api: TargetApi, private val db: TargetDB, private val pref:UserPreference):MainRepo {

    override suspend fun addToCart(item: LocalProduct) {
        CoroutineScope(Dispatchers.IO).launch {
            val cartItem = db.cartItemDao().getItemByProductId(item.id).firstOrNull()
            db.localProductDao().insertProduct(item.copy(qtyInCart = ++item.qtyInCart))
            if (cartItem == null){
                db.cartItemDao().insertItem(CartItems(id = item.id, qty = 1, name = item.name, price = item.price.toDouble(), image = item.image))
            }else {
                db.cartItemDao().insertItem(cartItem.copy(qty = ++cartItem.qty))
            }
        }
    }

    override suspend fun removeFromCart(item: LocalProduct) {
        CoroutineScope(Dispatchers.IO).launch {
            val cartItem = db.cartItemDao().getItemByProductId(item.id).firstOrNull()
            cartItem?.let {
                db.localProductDao().insertProduct(item.copy(qtyInCart = --item.qtyInCart))
                if (cartItem.qty <= 1){
                    db.cartItemDao().deleteItem(cartItem)
                }else{
                    db.cartItemDao().insertItem(cartItem.copy(qty = --cartItem.qty))
                }
            }
        }
    }

    override fun getProducts(): Flow<List<LocalProduct>> {
        return db.localProductDao().getAllProducts().map { prods->
            prods.filter { it.availability }
        }
    }

    override suspend fun getProductById(id: Int): Flow<LocalProduct> {
        try {
            val remote = api.getItemByID(id)
            if(remote.isSuccessful){
                remote.body()?.let { p->
                    db.localProductDao().insertProduct(LocalProduct(p.id,p.category,p.description,p.discount,p.image,p.name,p.price,p.quantity,p.unit,p.inStock,0, section = p.section))
                    for (t in p.tag){
                        db.productTagDao().insert(ProductTag(0,p.id,t))
                    }
                }
            }
        }catch (e:Exception){
            Constants.remoteError.value = "An Error has occurred while retrieving a product"
        }
        return db.localProductDao().getProductById(id)
    }

    override fun getProductsInCart(): Flow<List<CartItems>> {
        return db.cartItemDao().getAllCartItems()
    }

    override suspend fun fetchRemote(result: (Boolean, Exception?) -> Unit)  {
//        try {
//            val seasons = api.getAllSeasons()
//            if (seasons.isSuccessful){
//                for (season in seasons.body() as List){
//                    db.seasonDao().insertSeason(season)
//                }
//                val categories = api.getAllCategories()
//                if (categories.isSuccessful){
//                    for (category in categories.body() as List){
//                        db.categoryDao().insertCategory(category)
//                    }
//                    for (c in db.categoryDao().getCategories().first()){
//                        if (categories.body()?.contains(c) != true){
//                            db.categoryDao().delete(c)
//                        }
//                    }
//                    val banners = api.getAllBanners()
//                    if (banners.isSuccessful){
//                        for (banner in banners.body() as List){
//                            db.bannerDao().insertBanner(banner)
//                        }
//                        for (b in db.bannerDao().getAllBanners().first()){
//                            if (banners.body()?.contains(b) != true){
//                                db.bannerDao().delete(b)
//                            }
//                        }
//                        val items = api.getAllProducts()
//                        if (items.isSuccessful){
//                            for (item in items.body() as List){
//                                db.itemDao().insertItem(item)
//                            }
//                            result(true, null)
//                        }else {
//                            result(false, Exception("Items Error"))
//                        }
//                    }else{
//                        result(false,Exception("Categories Error"))
//                    }
//                }else{
//                    result(false,Exception("Banners Error"))
//                }
//            }else{
//                result(false,Exception("Season Error"))
//            }
//
//        }catch (e:Exception){
//            result(false,e)
//        }
    }

    override suspend fun getActiveSeason(): Flow<Season> {
        try {
            val remote = api.getAllSeasons()
            if(remote.isSuccessful){
                for (sea in remote.body() as List){
                    db.seasonDao().insertSeason(sea)
                }
            }
        }catch (e:Exception){
            Constants.remoteError.value = "An Error has occurred while retrieving season"
        }
        return db.seasonDao().getActiveSeason()
    }

    override suspend fun getCategories(onError:(Exception)->Unit): Flow<List<Category>> {
        try {
            val remote = api.getAllCategories()
            if(remote.isSuccessful){
                for (cat in remote.body() as List){
                    db.categoryDao().insertCategory(cat)
                }
                for (c in db.categoryDao().getCategories().first()){
                    if (remote.body()?.contains(c) != true){
                            db.categoryDao().delete(c)
                        }
                    }
            }
        }catch (e:Exception){
            onError(e)
            Constants.remoteError.value = "An Error has occurred while retrieving categories"
        }
        return db.categoryDao().getCategories()
    }

    override fun getBanners(): Flow<List<Banner>> {
        return db.bannerDao().getAllBanners()
    }

//    override suspend fun updateProduct(item: Product) {
//        db.itemDao().insertItem(item)
//    }

    override fun getAddresses(): Flow<List<Address>> {
        return db.addressDao().getAllAddress()
    }

    override suspend fun getProductsByCat(id: Int): Flow<List<LocalProduct>> = flow {
        var results: List<LocalProduct> = emptyList()
        try {
            val remote = api.getItemsByCategory(id)
            if (remote.isSuccessful){
                results = remote.body() as List
                for (prod in results){
                    db.localProductDao().insertProduct(prod)
                }
            }
        }catch (e:Exception){
            Log.e("ConnectionError", "getProductsByCat: ", e)
            emit(kotlin.collections.emptyList())
        }
        emit(results)
    }

    override fun getProductsByTags(tags: String): Flow<List<Product>> = flow {
        var results: List<Product> = emptyList()
        try {
            val remote = api.getProductsByTags(tags)
            if (remote.isSuccessful){
               results = remote.body() as List
            }
        }catch (e:Exception){
            Log.e("ConnectionError", "getProductsByCat: ", )
        }
        emit(results)
    }

    override suspend fun fetchDeals(): Flow<List<Product>> {
        try {
            val remote = api.getDeals()
            if(remote.isSuccessful){
                for (ban in remote.body() as List){
                    db.itemDao().insertItem(ban)
                }
            }
        }catch (e:Exception){
            Constants.remoteError.value = "An Error has occurred while retrieving deals"
        }
        return db.itemDao().getItems().map { items->
            TODO()
//            items.filter { it.tag.contains("deal") }
        }
    }

    override suspend fun fetchSpecial(): Flow<List<Product>> {
        try {
            val remote = api.getSpecial()
            if(remote.isSuccessful){
                for (ban in remote.body() as List){
                    db.itemDao().insertItem(ban)
                }
            }
        }catch (e:Exception){
            Constants.remoteError.value = "An Error has occurred while retrieving special products"
        }
        return db.itemDao().getItems().map { items->
            TODO()
//            items.filter { it.tag.contains("special", ignoreCase = true) }
        }
    }

    override suspend fun fetchRecommended(): Flow<List<Product>> {
        try {
            val remote = api.getRecommended()
            if(remote.isSuccessful){
                for (ban in remote.body() as List){
                    db.itemDao().insertItem(ban)
                }
            }
        }catch (e:Exception){
            Constants.remoteError.value = "An Error has occurred while retrieving recommended"
        }
        return db.itemDao().getItems().map { items->
            TODO()
//            items.filter { it.tag.contains("recom", ignoreCase = true) }
        }
    }

    override suspend fun fetchOffers(): Flow<List<Product>> {
        try {
            val remote = api.getOffers()
            if(remote.isSuccessful){
                for (ban in remote.body() as List){
                    db.itemDao().insertItem(ban)
                }
            }
        }catch (e:Exception){
            Constants.remoteError.value = "An Error has occurred while retrieving offers"
        }
        return db.itemDao().getItems().map { items->
            TODO()
//            items.filter { it.tag.contains("offer", ignoreCase = true) }
        }
    }

    override fun getNotifications(): Flow<List<TargetNotification>> {
        return db.notificationDao().getAll()
    }

    override suspend fun searchRemote(query: String): SearchResult {
        try {
            if (query.isEmpty()){
                return SearchResult()
            }
            val remote = api.search(query)
            if(remote.isSuccessful){
                val local = db.localProductDao().getAllProducts().first()
                remote.body()?.let {
                    val prods = it.products.map {p-> local[p.id] }
                    return SearchResult(products = prods, depts = it.depts)
                }
            }
        }catch (e:Exception){
            Constants.remoteError.value = "An Error has occurred while retrieving searching"
        }
        return SearchResult()
    }


    override suspend fun getCategoryById(id: Int): Category {
        val category = db.categoryDao().getCategoryByID(id)!!
//        try {
            val remote = api.getCategoryByID(id)
            if(remote.isSuccessful){
                db.categoryDao().insertCategory(remote.body() as Category)
            }
//        }catch (e:Exception){
////            Constants.remoteError.value = e.message
//            Log.e("Remote Error", "getCategoryById: ", e)
//        }
        return category
    }

    override suspend fun addAddress(address: Address, isAuto:Boolean) {
        if (isAuto){
            db.addressDao().getAutoAddress().collectLatest {
                db.addressDao().insertAddress(it.copy(village = address.village, street = address.street, phoneNumber = address.phoneNumber, isDefault = true))
            }
        }else {
            db.addressDao().insertAddress(address)
        }
    }

    override fun collectOrders(): Flow<List<OrderDetails>> {
        return db.orderDao().getAllOrders()
    }

    override suspend fun fetchAddress(spec: AddressSpec, result:(Exception?, List<AddResponse>)->Unit) {
        try {
            val remote = api.getAddress(spec)
            if (remote.isSuccessful){
                result(null, remote.body() ?: emptyList())
            }else{
                result(Exception(remote.message()), emptyList())
            }

        }catch (e:Exception){
            result(e, kotlin.collections.emptyList())
        }
    }

    override fun isFirstLaunch(): Boolean = pref.isNewUser()

    private fun handler(callback: (Exception) -> Unit) = CoroutineExceptionHandler{ _, throwable->
        callback(throwable as Exception)
    }

    override suspend fun fetchBanners(callback: (Exception?) -> Unit) {
        val job = SupervisorJob()
            CoroutineScope(Dispatchers.IO).launch(handler(callback)) {

                    val tags = api.getAllTags()
                    if (tags.isSuccessful){
                        for (t in tags.body()?.filter { tag-> tag.isActive } ?: emptyList()){
                            val banner = api.getBannerById(t.id)
                            if (banner.isSuccessful){
                                banner.body()?.let { b->
                                    try {
                                        db.bannerDao().insertBanner(b)
                                    }catch (e:Exception){

                                    }
                                    try {
                                        db.tagDao().insertTag(t)
                                    }catch (e:Exception){

                                    }

                                }
                            }else {
                                callback(Exception(tags.message()))
                            }
                        }
                        callback(null)
                    }else{
                        callback(Exception(tags.message()))
                    }

                    val remote = api.getAllProducts()
                    if (remote.isSuccessful){
                        val products = remote.body()
                        products?.let {
                            for (p in it){
                                db.localProductDao().insertProduct(LocalProduct(
                                    p.id, p.category, p.description, p.discount, p.image, p.name, p.price, p.quantity,p.unit,p.inStock,0,p.section
                                ))
                                for (t in p.tag){
                                    try {
                                        db.productTagDao().insert(ProductTag(productId = p.id, tagId = t))
                                    }catch (e:Exception){

                                    }
                                }
                            }
                        }
                    }else{
                        callback(Exception(remote.message()))
                    }
            }
    }

    override fun markUser() {
        pref.changeUserStatus(false)
    }

    override suspend fun getProductsByTagId(id:Int, callback: (Exception?) -> Unit): Flow<List<LocalProduct>> {
        val tags = db.productTagDao().getTagWithProduct(id).first()
        return db.localProductDao().getProductsByIds(tags.map { t->t.productId })
    }

    override fun getTagById(id: Int): Flow<Tag> {
        return db.tagDao().getTagById(id)
    }

    override fun getBannerById(id:Int): Flow<Banner> {
        return db.bannerDao().getBannerByID(id)
    }

    override fun getTagsByBannerId(id: Int): Flow<List<Tag>> {
        return db.tagDao().getTagsByBannerId(id)
    }

    override fun fetchLocations(callback: (Exception) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch(handler(callback)) {
            val district = api.getDistricts()
            val division = api.getDivision()
            val parish = api.getParish()
            val village = api.getVillage()
            val street = api.getStreet()
            if (district.isSuccessful){
                db.districtDao().insert(district.body() ?: emptyList())
            }
            if (division.isSuccessful){
                db.divisionDao().insert(division.body() ?: emptyList())
            }

            if(parish.isSuccessful){
                db.parishDao().insert(parish.body() ?: emptyList())
            }

            if(village.isSuccessful){
                db.villageDao().insert(village.body() ?: emptyList())
            }

            if(street.isSuccessful){
                db.streetDao().insert(street.body() ?: emptyList())
            }
        }
    }

    override suspend fun clearCart() {
        for (c in db.cartItemDao().getAllCartItems().first()){
            db.cartItemDao().deleteItem(c)
        }
        for (p in db.localProductDao().getAllProducts().first()){
            db.localProductDao().insertProduct(p.copy(qtyInCart = 0))
        }
    }

    override suspend fun insertNotes(notes: List<TargetNotification>) {
        db.notificationDao().insertAll(notes)
    }

    override suspend fun notificationMenuClick(action: String, notification: TargetNotification) {
        when(action){
            "Mark as read"->{
                db.notificationDao().insertAll(listOf(notification.copy(read = true)))
            }
            "Delete" -> {
                db.notificationDao().delete(notification)
            }
        }
    }

    override suspend fun getDeals(): Flow<List<Product>> {
        try {
            val remote = api.getDeals()
            if(remote.isSuccessful){
                for (ban in remote.body() as List){
                    db.itemDao().insertItem(ban)
                }
            }
        }catch (e:Exception){
            Constants.remoteError.value = "An Error has occurred while retrieving recommended"
        }
        return db.itemDao().getItems().map { items->
            TODO()
//            items.filter { it.tag.contains("deal", ignoreCase = true) }
        }
    }

    override suspend fun getSpecialProducts(): Flow<List<Product>> {
        try {
            val remote = api.getSpecial()
            if(remote.isSuccessful){
                for (ban in remote.body() as List){
                    db.itemDao().insertItem(ban)
                }
            }
        }catch (e:Exception){
            Constants.remoteError.value = "An Error has occurred while retrieving recommended"
        }
        return db.itemDao().getItems().map { items->
            TODO()
//            items.filter { it.tag.contains("special", ignoreCase = true) }
        }
    }


    override suspend fun getProductOffer(): Flow<List<Product>> {
        try {
            val remote = api.getOffers()
            if(remote.isSuccessful){
                for (ban in remote.body() as List){
                    db.itemDao().insertItem(ban)
                }
            }
        }catch (e:Exception){
            Constants.remoteError.value = "An Error has occurred while retrieving recommended"
        }
        return db.itemDao().getItems().map { items->
            TODO()
//            items.filter { it.tag.contains("offer", ignoreCase = true) }
        }
    }

}