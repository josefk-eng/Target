package com.target.supermarket.presentation.viewModels

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.target.supermarket.domain.models.Banner
import com.target.supermarket.domain.models.LocalProduct
import com.target.supermarket.domain.models.Product
import com.target.supermarket.domain.models.Tag
import com.target.supermarket.domain.repository.MainRepo
import com.target.supermarket.domain.usecases.categoryCases.CategoryCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val app:Application, private val cases: CategoryCases):
BaseViewModel<CategoryContract.CategoryState, CategoryContract.CategoryEvent, CategoryContract.CategoryEffect>(app){
    override fun initialiseState() = CategoryContract.CategoryState()

    init {
        onEvent()
    }

    private fun onEvent(){
        viewModelScope.launch {
            _event.collectLatest {
                when(it){
                    is CategoryContract.CategoryEvent.LoadTagInfo -> {
                        cases.getTagsByBannerId(it.id).collectLatest { t->
                            setState { copy(tags = t) }
                            cases.getBannerById(it.id).collectLatest { b->
                                delay(2000)
                                setState { copy(banner = b, bannerLoader = false) }
                                cases.getProductsByTag(_state.value.tags.first().id) { exc ->
                                    exc?.let {
                                        setEffect {
                                            CategoryContract.CategoryEffect.RemoteError(
                                                exc
                                            )
                                        }
                                    }
                                }.collectLatest { products->
                                    setState { copy(products = products, productsLoading = false) }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}

class CategoryContract{
    data class CategoryState(
        val bannerLoader:Boolean = true,
        val productsLoading:Boolean = true,
        val tags: List<Tag> = emptyList(),
        val products:List<LocalProduct> = emptyList(),
        val banner:Banner? = null
    ):ViewState

    sealed class CategoryEvent:ViewEvent{
        data class LoadTagInfo(val id:Int):CategoryEvent()
    }

    sealed class CategoryEffect:ViewEffect{
        data class RemoteError(val e:Exception):CategoryEffect()
    }
}