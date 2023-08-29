package com.target.supermarket.domain.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import com.target.supermarket.MainActivity
import com.target.supermarket.R
import com.target.supermarket.domain.models.TargetNotification
import com.target.supermarket.presentation.commons.getImage
import com.target.supermarket.presentation.navigation.Screen

private const val CHANNEL_ID = "Target_Channel"

fun Context.sendNotification(notification: TargetNotification){
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_ID,
            NotificationManager.IMPORTANCE_HIGH
        )
        val manager =  getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
        manager.sendAnotherNotifiaction(notification, this)

    }
}

fun NotificationManager.sendAnotherNotifiaction(notification: TargetNotification, applicationContext:Context){
    val taskDetailIntent = Intent(
        Intent.ACTION_VIEW,
        "cart://cartitems.com".toUri(),
        applicationContext,
        MainActivity::class.java
    )

    val pending: PendingIntent? = TaskStackBuilder.create(applicationContext).run {
        addNextIntentWithParentStack(taskDetailIntent)
        getPendingIntent(0, Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY)
    }
    val builder = NotificationCompat.Builder(
        applicationContext,
        CHANNEL_ID
    )
        .setSmallIcon(R.drawable.tlogo)
        .setContentTitle(notification.title)
        .setContentText(notification.description)
        .addAction(R.drawable.tlogo,"Check it out",pending)
//        .setLargeIcon(getBitmap(notification.image))

    notify(0,builder.build())
}

//private fun getBitmap(url:String):Bitmap{
//    val bitmap =
//}