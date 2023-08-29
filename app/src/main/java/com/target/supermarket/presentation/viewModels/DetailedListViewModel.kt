package com.target.supermarket.presentation.viewModels

import android.app.Application
import android.view.View
import androidx.lifecycle.viewModelScope
import com.target.supermarket.domain.models.Banner
import com.target.supermarket.domain.models.Category
import com.target.supermarket.domain.models.LocalProduct
import com.target.supermarket.domain.models.Product
import com.target.supermarket.domain.usecases.main.MainUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailedListViewModel @Inject constructor(
    private val useCases: MainUseCases,
    private val app:Application
    ): BaseViewModel<DetailedListContract.DetailedState, DetailedListContract.DetailedEvent, DetailedListContract.DetailedEffect>(app) {
    override fun initialiseState() = DetailedListContract.DetailedState()

    init {
        onEvent()
    }

    private fun onEvent(){
        viewModelScope.launch {
            _event.collectLatest { event->
                when(event){
                    is DetailedListContract.DetailedEvent.OnLoadContentByDepartment -> {
                        val cat = useCases.categoryById(event.catId)
                            setState { copy(category = cat) }
                            useCases.loadDept(event.catId).collectLatest {
                                setState { copy(mainList = it, loading = false) }
                            }

                    }
                    is DetailedListContract.DetailedEvent.OnLoadContentByTags -> {
//                        useCases.byTag(event.tags).collectLatest { prods->
//                            if (prods.isEmpty()){
//                                useCases.getItems().collectLatest { cache->
//                                    setState { copy(mainList = cache.filter {prod->
//                                        var include = false
//                                        for (tag in event.tags.split(",")){
//                                            include = prod.tag.split(",").contains(tag)
//                                        }
//                                        include
//                                    }, loading = false) }
//                                }
//                            }
//                            else {
//                                setState { copy(mainList = prods, loading = false) }
//                            }
//                        }
                    }
                    DetailedListContract.DetailedEvent.OnDoneLoading -> {
                        setState { copy(loadingMain = false) }
                    }
                }
            }
        }
    }

}

class DetailedListContract{
    data class DetailedState(
        val bannerImage:String = "",
        val mainList:List<LocalProduct> = emptyList(),
        val adList:List<LocalProduct> = emptyList(),
        val banners:List<Banner> = emptyList(),
        val title:String = "More on this..",
        val loading:Boolean = true,
        val category: Category? = null,
        val loadingMain:Boolean = true
    ):ViewState

    sealed class DetailedEffect:ViewEffect{
        object OnLoadingFinished: DetailedEffect()
    }

    sealed class DetailedEvent:ViewEvent{
        data class OnLoadContentByTags(val tags:String):DetailedEvent()
        data class OnLoadContentByDepartment(val catId: Int):DetailedEvent()
        object OnDoneLoading:DetailedEvent()
    }
}