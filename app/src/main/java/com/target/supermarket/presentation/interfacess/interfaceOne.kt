package com.target.supermarket.presentation.interfacess

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.target.supermarket.domain.models.LocalProduct
import com.target.supermarket.domain.models.Product
import com.target.supermarket.presentation.commons.getImage
import com.target.supermarket.presentation.navigation.Screen
import com.target.supermarket.presentation.viewModels.CommonContract
import com.target.supermarket.presentation.viewModels.CommonViewModel
import com.target.supermarket.ui.theme.lightBlue
import com.target.supermarket.utilities.CommonMethods
import com.target.supermarket.utilities.Loader
import com.valentinilk.shimmer.Shimmer

@Composable
fun InterfaceOne(
    modifier: Modifier,
    navController: NavHostController,
    shimmer: Shimmer,
    loader:Boolean = false,
    products: List<LocalProduct>
) {
    val width = LocalConfiguration.current.screenWidthDp


    val rem = width / 2
    val calW = if (rem > 250) (width/3.1).toInt()  else (width/2.1).toInt()
    val height = calW * 1.2
    LazyColumn(modifier = modifier.fillMaxSize()){
        items(products.chunked(if (rem>250) 3 else 2) ){prod->
            Row() {
                for (p in prod){
                    val image = getImage(url = p.image, onSuccess = {}) {

                    }
                    Card(shape = RoundedCornerShape(14.dp), modifier = Modifier
                        .padding(4.dp)
                        .width(calW.dp)
                        .height(height.dp).clickable {
                            CommonMethods.navigate(navController = navController, Screen.Product.passArg(p.id))
                        },) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(modifier = Modifier) {
                                Box(modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(10f)){
                                    Image(painter = image, contentDescription = "", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.FillBounds)
                                    Loader(shimmer = shimmer, isLoading = loader)
                                }
//                                Icon(painter = painterResource(id = com.target.supermarket.R.drawable.line_dotted), contentDescription = "", tint = lightBlue, modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp))
                                Text(text = p.name, modifier = Modifier
                                    .weight(5f)
                                    .padding(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}