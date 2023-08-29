package com.target.supermarket.domain.usecases

//import androidx.paging.ExperimentalPagingApi
//import androidx.paging.Pager
//import androidx.paging.PagingConfig
//import androidx.paging.PagingData
//import com.target.supermarket.data.local.room.TargetDB
//import com.target.supermarket.data.remote.TargetApi
//import com.target.supermarket.domain.ProductsMediator
//import com.target.supermarket.domain.models.Product
//import com.target.supermarket.presentation.viewModels.PAGE_SIZE
//import kotlinx.coroutines.flow.Flow
//
//class PagingSourceFromDB(private val db: TargetDB, private val api:TargetApi) {
//    @OptIn(ExperimentalPagingApi::class)
//    operator fun invoke(): Flow<PagingData<Product>> =
//        Pager(
//            config = PagingConfig(
//                pageSize = PAGE_SIZE,
//                prefetchDistance = 10,
//                initialLoadSize = PAGE_SIZE,
//            ),
//            pagingSourceFactory = {
//                db.itemDao().getMovies()
//            },
//            remoteMediator = ProductsMediator(
//                api, db),
//        ).flow
//}