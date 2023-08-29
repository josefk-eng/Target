package com.target.supermarket.domain.usecases.onBoardingCases

import com.target.supermarket.domain.repository.MainRepo

class MarkUser(private val repo:MainRepo) {
    operator fun invoke(){
        repo.markUser()
    }
}