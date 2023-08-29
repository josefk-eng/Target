package com.target.supermarket.utilities

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.target.supermarket.presentation.commons.getImage
import com.valentinilk.shimmer.Shimmer
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun CollapsibleScreenNew(
    modifier: Modifier = Modifier,
    imageUrl: String,
    imageLoader:Boolean,
    title:String,
    shimmer: Shimmer,
    navController:NavHostController,
    content:@Composable ()->Unit
) {
    val context = LocalContext.current
    val state = rememberCollapsingToolbarScaffoldState()
    val textSize = (18 + (30 - 18) * state.toolbarState.progress).sp
    val imageSize = (140 + (300 - 140) * state.toolbarState.progress)
    val image = getImage(url = imageUrl, onSuccess = {
        Toast.makeText(context,"Image Loaded Successfully",Toast.LENGTH_LONG).show()
    }) {
        Toast.makeText(context,"Image Loading Error",Toast.LENGTH_LONG).show()
    }

    CollapsingToolbarScaffold(
        modifier = Modifier.fillMaxSize(),
        state = state,
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        toolbar = {
            Card(modifier = Modifier
                .fillMaxWidth()
                .height(imageSize.dp)
                .parallax(0.5f), shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ){
                    if (imageLoader)
                        Image(painter = image,
                            contentDescription = "",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer {
                                    // change alpha of Image as the toolbar expands
                                    alpha = state.toolbarState.progress
                                })

                    Loader(shimmer = shimmer, isLoading = imageLoader )
                }
                Text(
                    text = title,
                    modifier = Modifier
                        .road(Alignment.CenterStart, Alignment.BottomEnd)
                        .padding(60.dp, 16.dp, 60.dp, 16.dp),
                    style = MaterialTheme.typography.h5.copy(color = if(textSize>18.sp) Color.White else Color.LightGray, fontWeight = FontWeight.Bold, fontSize = textSize),
                )
                Row(Modifier.fillMaxWidth()) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                    }
                }
            }
        }
    ){
        content()
    }

}