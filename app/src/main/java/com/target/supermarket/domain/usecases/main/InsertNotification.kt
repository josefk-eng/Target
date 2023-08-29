package com.target.supermarket.domain.usecases.main

import com.target.supermarket.domain.models.TargetNotification
import com.target.supermarket.domain.repository.MainRepo

class InsertNotification(private val repo:MainRepo) {
    suspend operator fun invoke(notes:List<TargetNotification>){
        repo.insertNotes(notes)
    }
}