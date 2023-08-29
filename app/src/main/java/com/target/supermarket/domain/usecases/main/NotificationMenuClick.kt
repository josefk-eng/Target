package com.target.supermarket.domain.usecases.main

import com.target.supermarket.domain.models.TargetNotification
import com.target.supermarket.domain.repository.MainRepo

class NotificationMenuClick(private val repo:MainRepo) {
    suspend operator fun invoke(action:String, notification: TargetNotification){
        repo.notificationMenuClick(action, notification)
    }
}