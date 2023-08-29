package com.target.supermarket.domain.usecases.main

import com.target.supermarket.domain.repository.MainRepo

class FetchRemote (private val repo:MainRepo) {
    suspend operator fun invoke(result:(Boolean, Exception?)->Unit){
        repo.fetchRemote(result)
    }
}