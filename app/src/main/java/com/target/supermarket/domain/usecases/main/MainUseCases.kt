package com.target.supermarket.domain.usecases.main

import com.target.supermarket.domain.usecases.checkout.ChangeDefaultAddress
import com.target.supermarket.presentation.viewModels.CommonContract

//import com.target.supermarket.domain.usecases.PagingSourceFromDB

data class MainUseCases(
    val addToCart: AddToCart,
    val removeFromCart: RemoveFromCart,
    val evaluateCart: EvaluateCart,
    val getItems: GetItems,
    val fetchRemote: FetchRemote,
    val getSeason: GetSeason,
    val getCategories: GetCategories,
    val getAddress: GetAddressGlobal,
    val recommended: Recommended,
    val deals: Deals,
    val specialProducts: SpecialProducts,
    val offers: Offers,
    val loadDept: LoadDept,
    val byTag: GetProductsByTag,
    val getNotifications: GetNotifications,
    val searchCase: SearchCase,
    val categoryById: GetCategoryById,
    val addLocation: AddLocation,
    val collectOrders: CollectOrders,
    val fetchAddress: FetchAddress,
    val clearCart: ClearCart,
    val insertNotification: InsertNotification,
    val notificationMenuClick: NotificationMenuClick
)