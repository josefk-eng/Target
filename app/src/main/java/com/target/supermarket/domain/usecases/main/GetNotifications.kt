package com.target.supermarket.domain.usecases.main

import com.target.supermarket.domain.models.TargetNotification
import com.target.supermarket.domain.repository.MainRepo
import kotlinx.coroutines.flow.Flow

class GetNotifications(private val repo:MainRepo) {
    operator fun invoke(): Flow<List<TargetNotification>> {
        return repo.getNotifications()
    }
}