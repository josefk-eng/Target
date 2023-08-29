package com.target.supermarket.domain.di

import com.target.supermarket.data.local.room.TargetDB
import com.target.supermarket.data.remote.TargetApi
import com.target.supermarket.domain.ProductSource
import com.target.supermarket.domain.repository.CheckOutRepository
import com.target.supermarket.domain.usecases.main.*
import com.target.supermarket.domain.usecases.product.GetProductUseCase
import com.target.supermarket.domain.usecases.product.ProductsUseCases
import com.target.supermarket.domain.usecases.product.getRelatedProducts
import com.target.supermarket.domain.repository.MainRepo
import com.target.supermarket.domain.repository.ProductRepository
import com.target.supermarket.domain.usecases.*
import com.target.supermarket.domain.usecases.categoryCases.*
import com.target.supermarket.domain.usecases.checkout.*
import com.target.supermarket.domain.usecases.homeCases.GetBanners
import com.target.supermarket.domain.usecases.homeCases.GetProducts
import com.target.supermarket.domain.usecases.homeCases.HomeCases
import com.target.supermarket.domain.usecases.launcherCases.CheckFirstLaunch
import com.target.supermarket.domain.usecases.launcherCases.FetchBanners
import com.target.supermarket.domain.usecases.launcherCases.FetchLocations
import com.target.supermarket.domain.usecases.launcherCases.LauncherCases
import com.target.supermarket.domain.usecases.onBoardingCases.MarkUser
import com.target.supermarket.domain.usecases.onBoardingCases.OnBoardingCases
import com.target.supermarket.domain.usecases.product.GetCartItemById
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

//    @Provides
//    @Singleton
//    fun providesHomeUseCases(repo:HomeRepository):HomeUseCases = HomeUseCases(
//        getBanners = GetBannersTemp(repo),
//    )

    @Provides
    @Singleton
    fun provideProductUseCases(repo:ProductRepository): ProductsUseCases = ProductsUseCases(
        getProduct = GetProductUseCase(repo),
        getRelatedProducts = getRelatedProducts(repo),
        getCartItemById = GetCartItemById(repo)
    )

    @Provides
    @Singleton
    fun providesMainUseCase(repo:MainRepo): MainUseCases = MainUseCases(
        addToCart = AddToCart(repo),
        removeFromCart = RemoveFromCart(repo),
        evaluateCart = EvaluateCart(repo),
        getItems = GetItems(repo),
        fetchRemote =  FetchRemote(repo),
        getCategories = GetCategories(repo),
        getSeason = GetSeason(repo),
        getAddress = GetAddressGlobal(repo),
        recommended = Recommended(repo),
        deals = Deals(repo),
        specialProducts = SpecialProducts(repo),
        offers = Offers(repo),
        loadDept = LoadDept(repo),
        byTag = GetProductsByTag(repo),
        getNotifications = GetNotifications(repo),
        searchCase = SearchCase(repo),
        categoryById = GetCategoryById(repo),
        addLocation = AddLocation(repo),
        collectOrders = CollectOrders(repo),
        fetchAddress = FetchAddress(repo),
        clearCart = ClearCart(repo),
        insertNotification = InsertNotification(repo),
        notificationMenuClick = NotificationMenuClick(repo)
    )

    @Provides
    @Singleton
    fun providesCheckOutCases(repo:CheckOutRepository) = CheckOutUseCases(
        addPaymentNumber = AddPaymentNumber(repo),
        getPayNumbers = GetPayNumbers(repo),
        updateNumber = UpdateNumber(repo),
        placeOrder = PlaceOrder(repo),
        addOrder = AddOrder(repo),
        getDistricts = GetDistricts(repo),
        getDivisions = GetDivisions(repo),
        getParish = GetParish(repo),
        getVillages = GetVillages(repo),
        getStreets = GetStreets(repo),
        addAddress = AddAddress(repo),
        getAddress = GetAddress(repo),
        changeDefaultAddress = ChangeDefaultAddress(repo),
        deleteAddress = DeleteAddress(repo)
    )


    @Provides
    @Singleton
    fun providesSource(api: TargetApi, db: TargetDB):ProductSource = ProductSource(api, db)


    @Provides
    @Singleton
    fun providesLauncher(repo:MainRepo):LauncherCases = LauncherCases(
        CheckFirstLaunch(repo),
        FetchBanners(repo),
        FetchLocations(repo)
    )

    @Provides
    @Singleton
    fun providesOnBoarding(repo:MainRepo):OnBoardingCases = OnBoardingCases(
        MarkUser(repo)
    )

    @Provides
    @Singleton
    fun providesHomeCases(repo: MainRepo):HomeCases = HomeCases(
        getBanners = GetBanners(repo),
        getProducts = GetProducts(repo)
    )

    @Provides
    @Singleton
    fun providesCategoryCases(repo: MainRepo):CategoryCases = CategoryCases(
        getBannerById = GetBannerById(repo),
        getProductsByTag = GetProductsByTagId(repo),
        getTagsByBannerId = GetTagsByBannerId(repo)
    )

}