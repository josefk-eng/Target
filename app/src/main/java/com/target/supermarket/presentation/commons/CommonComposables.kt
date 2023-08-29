package com.target.supermarket.presentation.commons

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.request.CachePolicy
import com.target.supermarket.utilities.CommonMethods
import com.target.supermarket.R

@OptIn(ExperimentalCoilApi::class)
@Composable
fun getImage(url:Any, onSuccess:()->Unit, onError:()->Unit): Painter =
    rememberImagePainter(data = if (url is String){
    if (url.contains("http")){
        url
    }else {
        CommonMethods.appendUrl(url as String) ?: ""
    }
}
else url){
    diskCachePolicy(policy = CachePolicy.ENABLED)
    crossfade(true)
    listener(
        onError = {r,th->
            onError()
        },
        onSuccess = {r,m->
            onSuccess()
        }
    )
}