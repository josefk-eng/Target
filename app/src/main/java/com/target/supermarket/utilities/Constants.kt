package com.target.supermarket.utilities

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData

object Constants {
    const val BASE_URL = "https://apis.jonestah.com"
//    const val BASE_URL = "http://10.0.2.2:5000/"
//    const val BASE_URL = "http://192.168.188.11:5000/"
//    const val BASE_URL = "http://172.20.10.7:5000/"
    val connectionError: MutableLiveData<String> = MutableLiveData("")
    val remoteError: MutableLiveData<String> = MutableLiveData("")
    val addressError: MutableLiveData<String> = MutableLiveData("")
//    val p_style = MaterialTheme.typography.h6.copy(
//        fontWeight = FontWeight.ExtraBold,
//        fontSize = 12.sp
//    )
}