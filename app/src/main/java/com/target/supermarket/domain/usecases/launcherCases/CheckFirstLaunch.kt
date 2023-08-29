package com.target.supermarket.domain.usecases.launcherCases

import com.target.supermarket.domain.repository.MainRepo

class CheckFirstLaunch(private val repo: MainRepo) {
    operator fun invoke() = repo.isFirstLaunch()
}