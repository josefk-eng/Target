package com.target.supermarket.domain.usecases.launcherCases

import com.target.supermarket.domain.usecases.onBoardingCases.MarkUser

data class LauncherCases(
    val checkFirstLaunch: CheckFirstLaunch,
    val fetchBanners: FetchBanners,
    val fetchLocations: FetchLocations
)
