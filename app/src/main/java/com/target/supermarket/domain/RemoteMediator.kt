package com.target.supermarket.domain

//import androidx.paging.ExperimentalPagingApi
//import androidx.paging.LoadType
//import androidx.paging.PagingState
//import androidx.paging.RemoteMediator
//import androidx.room.withTransaction
//import com.target.supermarket.data.local.room.TargetDB
//import com.target.supermarket.data.remote.TargetApi
//import com.target.supermarket.domain.models.Product
//import com.target.supermarket.domain.models.RemoteKeys
//import retrofit2.HttpException
//import java.io.IOException
//import java.util.concurrent.TimeUnit
//
//@OptIn(ExperimentalPagingApi::class)
//class ProductsMediator(private val api: TargetApi, private val db: TargetDB): RemoteMediator<Int, Product>() {
//
//    override suspend fun initialize(): InitializeAction {
//        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
//        return if (System.currentTimeMillis() - (db.getRemoteKeysDao().getCreationTime() ?: 0) < cacheTimeout) {
//            InitializeAction.SKIP_INITIAL_REFRESH
//        } else {
//            InitializeAction.LAUNCH_INITIAL_REFRESH
//        }
//    }
//
//
//    override suspend fun load(
//        loadType: LoadType,
//        state: PagingState<Int, Product>
//    ): MediatorResult {
//
//        val page: Int = when (loadType) {
//            LoadType.REFRESH -> {
//                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
//                remoteKeys?.nextKey?.minus(1) ?:1
//            }
//            LoadType.PREPEND -> {
//                val remoteKeys = getRemoteKeyForFirstItem(state)
//                val prevKey = remoteKeys?.prevKey
//                prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
//            }
//            LoadType.APPEND -> {
//                val remoteKeys = getRemoteKeyForLastItem(state)
//                val nextKey = remoteKeys?.nextKey
//                nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
//            }
//        }
//
//        try {
//            val apiResponse = api.getAllProducts(page = page)
//            val products = apiResponse.data
//            val endOfPaginationReached = products.isEmpty()
//
//            db.withTransaction {
//                if (loadType == LoadType.REFRESH){
//                    db.getRemoteKeysDao().clearRemoteKeys()
//                    db.itemDao().clearAllMovies()
//                }
//
//                val prevKey = if (page > 1) page - 1 else null
//                val nextKey = if (endOfPaginationReached) null else page + 1
//                val remoteKeys = products.map {
//                    RemoteKeys(productID = it.id, prevKey = prevKey, currentPage = page, nextKey = nextKey)
//                }
//                db.getRemoteKeysDao().insertAll(remoteKeys)
//                db.itemDao().insertAll(products)
//            }
//
//            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
//        }catch (error: IOException) {
//            return MediatorResult.Error(error)
//        } catch (error: HttpException) {
//            return MediatorResult.Error(error)
//        }
//    }
//
//
//    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Product>): RemoteKeys? {
//        return state.anchorPosition?.let { position ->
//            state.closestItemToPosition(position)?.id?.let { id ->
//                db.getRemoteKeysDao().getRemoteKeyByMovieID(id)
//            }
//        }
//    }
//
//
//    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Product>): RemoteKeys? {
//        return state.pages.firstOrNull {
//            it.data.isNotEmpty()
//        }?.data?.firstOrNull()?.let { product ->
//            db.getRemoteKeysDao().getRemoteKeyByMovieID(product.id)
//        }
//    }
//
//    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Product>): RemoteKeys? {
//        return state.pages.lastOrNull {
//            it.data.isNotEmpty()
//        }?.data?.lastOrNull()?.let { movie ->
//            db.getRemoteKeysDao().getRemoteKeyByMovieID(movie.id)
//        }
//    }
//
//}