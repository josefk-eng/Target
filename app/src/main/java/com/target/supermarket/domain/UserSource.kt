package com.target.supermarket.domain

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.target.supermarket.data.local.room.TargetDB
import com.target.supermarket.data.remote.TargetApi
import com.target.supermarket.domain.models.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ProductSource(private val api: TargetApi, private val db: TargetDB) : PagingSource<Int, Product>() {

    override fun getRefreshKey(state: PagingState<Int, Product>): Int?
    {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>):LoadResult<Int, Product> {
        return try {
            val nextPage = params.key ?: 1
            val products = api.getAllProductsPerPage(page = nextPage)
            CoroutineScope(Dispatchers.IO).launch {
                db.itemDao().insertAllProducts(products.data)
            }
            LoadResult.Page(
                data = products.data,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (products.data.isEmpty()) null else products.page + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}