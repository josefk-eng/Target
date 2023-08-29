package com.target.supermarket.presentation.viewModels

import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.target.supermarket.data.local.UserPreference
import com.target.supermarket.domain.ProductSource
import com.target.supermarket.domain.models.Address
import com.target.supermarket.domain.models.CartItems
import com.target.supermarket.domain.models.*
import com.target.supermarket.domain.usecases.main.MainUseCases
import com.target.supermarket.utilities.Items
import com.target.supermarket.utilities.ItemsLoadingStatus
import com.target.supermarket.workers.TokenAnalyser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.reflect.KClass


@HiltViewModel
class CommonViewModel @Inject constructor(private val prefs: UserPreference, private val useCases: MainUseCases, private val source: ProductSource, private val app: Application):
    BaseViewModel<CommonContract.CommonState,CommonContract.CommonEvent, CommonContract.CommonEffect>(app) {
    override fun initialiseState(): CommonContract.CommonState = CommonContract.CommonState(
//        productsReload = Pager(PagingConfig(pageSize = 10)){ source }.flow.cachedIn(viewModelScope),
        showErrorDialog = true
    )
    val searchText = MutableStateFlow("")
    var result:Job? = null

    init {
        onEvent()

//        startWorker()
//        pagingSource()
        viewModelScope.launch {
//            launch { fetchCategories {  } }  // getCategories()
//            launch { fetchSeasons {  } }
//            launch { fetchBanners {  } }
//            launch { fetchRecomended {  } } //fetchRecommended
//            launch { fetchDeals {  } } //fetchDeals
//            launch { fetchSpecial {  } }  //fetchSpecial
//            launch { fetchOffer {  } } //fetchOffers
//            launch { fetchAddress {  } } //getAddresses
//            launch { getNotifications() } //getNotifications
//            launch { fetchOrders() }
            getAllProducts()

        }
//        searchQuery()
        getNotifications()
        fetchAddress {  }
    }



    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private fun searchQuery(){
        result?.cancel()
        result = searchText.debounce(2000)
            .distinctUntilChanged()
            .flatMapLatest {
                queryFromRemote(it)
            }.launchIn(viewModelScope)
    }

    private suspend fun queryFromRemote(query: String): Flow<SearchResult> {
        return if (query.isNotEmpty()) {
            val result = useCases.searchCase(query)
            result.collectLatest {
                setState { copy(searchResult = it, searching = false) }
            }
            result
        }else {
            flow { emit(SearchResult()) }
        }
    }

    private fun startWorker() {
        val request = OneTimeWorkRequestBuilder<TokenAnalyser>().build()
        val workManager = WorkManager.getInstance(app)
        workManager.enqueue(request)
    }

    private fun onEvent(){
        viewModelScope.launch {
            _event.collectLatest { event->
                when(event){
                    is CommonContract.CommonEvent.AddToCart -> {
                        viewModelScope.launch {
                            useCases.addToCart(event.product)
                        }
                    }
                    is CommonContract.CommonEvent.RemoveFromCart -> {
                        viewModelScope.launch {
                            useCases.removeFromCart(event.product)
                        }
                    }
                    is CommonContract.CommonEvent.ChooseCategory -> {
                        setState { copy(category = null) }
                        useCases.loadDept(event.category.id).collectLatest {
                            delay(2000)
                            setState { copy(category = Pair(event.category,it)) }
                        }
                    }
                    is CommonContract.CommonEvent.ChooseProduct -> {
                        setState { copy(product = event.product) }
                    }
                    is CommonContract.CommonEvent.OnLocationFound -> {
                        useCases.addLocation(event.location, isAuto = true)
//                        val ad = Address(address = event.location, nearbyPlace = "", phoneNumber = "", isDefault = true, street = "")
//                        val ads = _state.value.addresses.toMutableList()
//                        ads.add(ad)
//                        setState { copy(addresses = ads) }
                    }
                    CommonContract.CommonEvent.RestartLoop -> {
                        setState { copy(banners = _state.value.banners
                            .asSequence()
                            .shuffled()
                            .drop(1)
                            .toList()) }
                    }
                    is CommonContract.CommonEvent.OpenAccDialog -> {
                        setState { copy(accDialogStatus = event.open) }
                    }
                    is CommonContract.CommonEvent.OpenNotificationDialog -> {
                        setState { copy(notificationDialogStatus = event.open) }
                        if (!event.open){
                            event.onClose()
                        }
                    }
                    CommonContract.CommonEvent.OnSearchTextChanged -> {
                        if (searchText.value.isNotEmpty()) {
                            setState { copy(searching = true) }
                            searchQuery()
                        }else{
                            setState { copy(searchResult = null, searching = false) }
                        }
                    }
                    CommonContract.CommonEvent.DialogShown -> {
                        setState { copy(showErrorDialog = false) }
                    }
                    is CommonContract.CommonEvent.OnConnectionChanged -> {
                        setState { copy(isConnected = event.connected) }
                    }
                    CommonContract.CommonEvent.OnCheckOut -> {
                        delay(1000)
                        setEffect { CommonContract.CommonEffect.OnCheckOut }
                    }
                    CommonContract.CommonEvent.FinalCleanUp -> {
                        useCases.clearCart()
                        setState { copy(to = true) }
                    }
                    is CommonContract.CommonEvent.OnInsertNotifications -> {
                        useCases.insertNotification(event.notes)
                    }
                    CommonContract.CommonEvent.AutoNavigateToOrder -> {
                        setState { copy(to = !_state.value.to) }
                    }
                    is CommonContract.CommonEvent.NotificationMenuClick -> {
                        useCases.notificationMenuClick(event.action,event.notification)
                    }
                }
            }
        }
    }

    private suspend fun evalCart(){
        useCases.evaluateCart().collectLatest {cartItems->
            setState { copy(cartItems = cartItems) }
            useCases.collectOrders().collectLatest {
                setState { copy(orders = it) }
            }
        }
    }

    private suspend fun getAllProducts(){
        useCases.getItems().collectLatest {
            setState { copy(products = it.toMutableList()) }
            evalCart()
        }
    }


    private suspend fun fetchSeasons(fetched:()->Unit){
        useCases.getSeason().collectLatest { season->
            setState { copy(season = season) }
            fetched()
        }
    }

    private suspend fun fetchCategories(fetched:()->Unit){

        val loadStatus = _state.value.loaders
//        delay(2000)
        useCases.getCategories{
            viewModelScope.launch {
                loadStatus[Items.Categories] = ItemsLoadingStatus.Error("Oops something went wrong")
                setState { copy(loaders = loadStatus) }
            }
        }.collectLatest { cats->
            val cate = cats.sortedBy { it.id }
            setState { copy(categories = cate) }
            if (cate.isEmpty()){
                loadStatus[Items.Categories] = ItemsLoadingStatus.Error("Oops no Categories found")
            }else{
                loadStatus[Items.Categories] = ItemsLoadingStatus.Loaded
            }
            setState { copy(loaders = loadStatus) }
            fetched()
        }
    }

//    private suspend fun fetchRecomended(fetched:()->Unit){
//        delay(2000)
//        val loadStatus = _state.value.loaders
//        useCases.recommended().collectLatest { recommends->
//            loadStatus[Items.Recommended] = ItemsLoadingStatus.Loaded
//            _state.value.products.addAll(recommends)
//            setState { copy(recommended = recommends, loaders = loadStatus) }
//            fetched()
//        }
//    }

//    private suspend fun fetchDeals(fetched:()->Unit){
//        delay(1000)
//        val loadStatus = _state.value.loaders
//        useCases.deals().collectLatest { deals->
//            loadStatus[Items.Deals] = ItemsLoadingStatus.Loaded
//            _state.value.products.addAll(deals)
//            setState { copy(deals = deals, loaders = loadStatus) }
//            fetched()
//        }
//    }

//    private suspend fun fetchSpecial(fetched:()->Unit){
//        delay(1000)
//        val loadStatus = _state.value.loaders
//        useCases.specialProducts().collectLatest { special->
//            loadStatus[Items.Special] = ItemsLoadingStatus.Loaded
//            _state.value.products.addAll(special)
//            setState { copy(special = special, loaders = loadStatus) }
//            fetched()
//        }
//    }

//    private suspend fun fetchOffer(fetched:()->Unit){
//        delay(1000)
//        val loadStatus = _state.value.loaders
//        useCases.offers().collectLatest { offers->
//            loadStatus[Items.Offer] = ItemsLoadingStatus.Loaded
//            _state.value.products.addAll(offers)
//            setState { copy(offers = offers, loaders = loadStatus) }
//            fetched()
//        }
//    }

    private fun fetchAddress(fetched:()->Unit){
        viewModelScope.launch {
            useCases.getAddress().collectLatest { address->
                setState { copy(addresses = address) }
                fetched()
            }
        }
    }

    private fun getNotifications(){
        viewModelScope.launch {
            useCases.getNotifications().collectLatest {
                setState { copy(notifications = it) }
                for (n in it.filter { o-> !o.notified }){
                    setEffect { CommonContract.CommonEffect.OnSendNotification(n) }
                    useCases.insertNotification(listOf(n.copy(notified = true)))
                }
            }
        }
    }

}

class CommonContract{
    data class CommonState(
        val cartItems: List<CartItems> = emptyList(),
        val products: MutableList<LocalProduct> = mutableListOf(),
//        val productsReload: Flow<PagingData<Product>>,
        val categories:List<Category> = emptyList(),
        val banners:List<Banner> = emptyList(),
        val relatedProducts:List<Product> = emptyList(),
        val remoteError:String = "",
        val loading:Boolean = false,
        val season: Season? = null,
        val banner: Banner? = null,
        val addresses:List<Address> = listOf(),
        val category: Pair<Category, List<LocalProduct>>? = null,
        val product: LocalProduct? = null,
        val recommended: List<Product> = emptyList(),
        val deals: List<LocalProduct> = emptyList(),
        val special: List<Product> = emptyList(),
        val offers: List<Product> = emptyList(),
        val inCategory:List<LocalProduct> = emptyList(),
        val detailedList:List<Product> = emptyList(),
        val isSet:Boolean = false,
        val orders:List<OrderDetails> = emptyList(),
        val notifications:List<TargetNotification> = emptyList(),
        val accDialogStatus:Boolean = false,
        val notificationDialogStatus:Boolean = false,
        val searchText:String = "",
        val searchResult:SearchResult? = null,
        val searching:Boolean = false,
        val showErrorDialog:Boolean,
        val isConnected:Boolean = true,
        val to:Boolean = false,
        val loaders: MutableMap<Items, Any> = mutableMapOf(
            Pair(Items.Categories,ItemsLoadingStatus.Loading),
            Pair(Items.FeaturedCategories,ItemsLoadingStatus.Loading),
            Pair(Items.Recommended,ItemsLoadingStatus.Loading),
            Pair(Items.Deals,ItemsLoadingStatus.Loading),
            Pair(Items.Special,ItemsLoadingStatus.Loading),
            Pair(Items.Offer,ItemsLoadingStatus.Loading),
        )

    ):ViewState

    sealed class CommonEffect:ViewEffect{
        data class OnLoadingFinished(val onFirstLaunch:Boolean):CommonEffect()
        object OnCheckOut:CommonEffect()
        data class OnSendNotification(val notification:TargetNotification):CommonEffect()
    }

    sealed class CommonEvent:ViewEvent{
        data class AddToCart(val product: LocalProduct):CommonEvent()
        data class RemoveFromCart(val product: LocalProduct):CommonEvent()
        data class ChooseCategory(val category: Category):CommonEvent()
        data class ChooseProduct(val product: LocalProduct):CommonEvent()
        data class OnLocationFound(val location: android.location.Address):CommonEvent()
        data class OpenAccDialog(val open: Boolean) : CommonEvent()
        data class OpenNotificationDialog(val open: Boolean, val onClose:()->Unit) : CommonEvent()
        data class OnConnectionChanged(val connected: Boolean) : CommonEvent()
        data class OnInsertNotifications(val notes: List<TargetNotification>) : CommonEvent()
        object RestartLoop : CommonEvent()
        object DialogShown : CommonEvent()
        object OnSearchTextChanged : CommonEvent()
        object OnCheckOut : CommonEvent()
        object FinalCleanUp : CommonEvent()
        object AutoNavigateToOrder : CommonEvent()
        data class NotificationMenuClick(val action:String, val notification: TargetNotification):CommonEvent()
    }
}


//is CommonContract.CommonEvent.ChangeDefaultAddress -> {
////                        _state.value.addresses.firstOrNull { it.isDefault }?.let { address ->
////                            useCases.addAddress(address.copy(isDefault = false))
////                        }
////                        delay(2000)
////                        useCases.addAddress(event.address.copy(isDefault = true))
//}