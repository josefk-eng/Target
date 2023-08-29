package com.target.supermarket.presentation.product

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.target.supermarket.domain.models.CartItems
import com.target.supermarket.domain.models.LocalProduct
import com.target.supermarket.domain.usecases.product.ProductsUseCases
import com.target.supermarket.presentation.viewModels.BaseViewModel
import com.target.supermarket.presentation.viewModels.ViewEffect
import com.target.supermarket.presentation.viewModels.ViewEvent
import com.target.supermarket.presentation.viewModels.ViewState
import com.target.supermarket.domain.models.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(private val app:Application,private val useCase: ProductsUseCases): BaseViewModel<ProductsContract.ProductState, ProductsContract.ProductsEvent, ProductsContract.ProductsEffect>(app) {
    override fun initialiseState(): ProductsContract.ProductState = ProductsContract.ProductState()

    init {
        onEvent()
    }

    fun getProduct(id:Int){
        viewModelScope.launch {
            useCase.getProduct(id).collectLatest {
                setState { copy(product = it) }
                getCartItem(it.id)
//                getRelatedProducts(it)
            }
        }
    }

    private fun getRelatedProducts(item: LocalProduct){
        viewModelScope.launch {
            useCase.getRelatedProducts(item).collectLatest {
                setState { copy(otherProducts = it) }
            }
        }
    }

    private suspend fun getCartItem(id: Int){
        useCase.getCartItemById(id).collectLatest {
            setState { copy(cartItem = it) }
        }
    }


    private fun onEvent(){
        viewModelScope.launch {
            _event.collectLatest {
                when(it){
                    is ProductsContract.ProductsEvent.GetProduct -> getProduct(it.id)
                }
            }
        }
    }



}

class ProductsContract{
    data class ProductState(
        val product:LocalProduct? = null,
        val otherProducts:List<LocalProduct> = emptyList(),
        val cartItem:CartItems? = null
    ):ViewState

    sealed class ProductsEvent: ViewEvent {
        data class GetProduct(val id: Int):ProductsEvent()
    }

    sealed class ProductsEffect: ViewEffect {

    }
}