package com.target.supermarket.presentation.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.target.supermarket.domain.models.*
import com.target.supermarket.domain.usecases.homeCases.HomeCases
import com.target.supermarket.utilities.Items
import com.target.supermarket.utilities.ItemsLoadingStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val app:Application, private val cases: HomeCases):
BaseViewModel<HomeContract.HomeState, HomeContract.HomeEvent, HomeContract.HomeEffect>(app){
    override fun initialiseState() = HomeContract.HomeState()

    init {
        initialize()
        onEvent()
    }

    private fun initialize(){
        viewModelScope.launch {
            launch {
                cases.getBanners().collectLatest {
                    setState { copy(visibleBanner = it.firstOrNull(),banners = it) }
                    delay(2000)
                    setState { copy(bannersLoading = false) }
                }
            }
            launch {
                val loadStatus = _state.value.loaders
                cases.getProducts().collectLatest {
                    try {
                        loadStatus[Items.Recommended] = ItemsLoadingStatus.Loaded
                        setState { copy(recommended = it.filter { p-> p.section == "RECOM" }.shuffled()) }
                        delay(2000)
                        loadStatus[Items.Deals] = ItemsLoadingStatus.Loaded
                        setState { copy(deals = it.filter { p-> p.section=="DEAL" }.shuffled(), loaders = loadStatus) }
                        delay(2000)
                        loadStatus[Items.Special] = ItemsLoadingStatus.Loaded
                        setState { copy(special = it.filter { p-> p.section=="SPEC" }.shuffled(), loaders = loadStatus) }
                    }catch (e:Exception){
                        Log.e("TAG", "initialize: ", )
                    }
                }
            }
        }
    }

    private fun onEvent(){
        viewModelScope.launch {
            _event.collectLatest {
                when(it){
                    HomeContract.HomeEvent.ChangeMainBanner -> {
                        setState { copy(visibleBanner = _state.value.banners
                            .asSequence()
                            .shuffled()
                            .toList()
                            .firstOrNull()) }
                    }
                }
            }
        }
    }

}

class HomeContract{
    data class HomeState(
        val categories:List<Category> = emptyList(),
        val banners:List<Banner> = emptyList(),
        val visibleBanner:Banner? = null,
        val categoriesLoading:Boolean = true,
        val bannersLoading:Boolean = true,
        val recommended: List<LocalProduct> = emptyList(),
        val deals: List<LocalProduct> = emptyList(),
        val special: List<LocalProduct> = emptyList(),
        val loaders: MutableMap<Items, Any> = mutableMapOf(
            Pair(Items.Categories, ItemsLoadingStatus.Loading),
            Pair(Items.FeaturedCategories, ItemsLoadingStatus.Loading),
            Pair(Items.Recommended, ItemsLoadingStatus.Loading),
            Pair(Items.Deals, ItemsLoadingStatus.Loading),
            Pair(Items.Special, ItemsLoadingStatus.Loading),
            Pair(Items.Offer, ItemsLoadingStatus.Loading),
        )
    ):ViewState

    sealed class HomeEvent:ViewEvent{
        object ChangeMainBanner : HomeEvent()
    }

    sealed class HomeEffect:ViewEffect{

    }
}