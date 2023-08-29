package com.target.supermarket.utilities

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.util.Log
import android.widget.Space
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.target.supermarket.R
import com.target.supermarket.domain.models.LocalProduct
import com.target.supermarket.domain.models.PaymentNumbers
import com.target.supermarket.presentation.cart.TextButtonCustom
import com.target.supermarket.presentation.commons.TextInputWidget
import com.target.supermarket.presentation.commons.TransparentInput
import com.target.supermarket.presentation.commons.getImage
import com.target.supermarket.presentation.home.deals.AddButton
import com.target.supermarket.presentation.navigation.Screen
import com.target.supermarket.presentation.viewModels.CommonContract
import com.target.supermarket.presentation.viewModels.CommonViewModel
import com.target.supermarket.presentation.viewModels.DeliveryInfoContact
import com.target.supermarket.presentation.viewModels.DeliveryInfoViewModel
import com.target.supermarket.ui.theme.errorColor
import com.target.supermarket.ui.theme.lightBlue
import com.target.supermarket.ui.theme.lighterGray
import com.target.supermarket.ui.theme.topAppBar
import com.target.supermarket.utilities.Constants.addressError
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import java.util.*


fun <T : Any> List<T>.mapAutoScrollItem(): List<AutoScrollItem<T>> {
    val newList = this.map { AutoScrollItem(data = it) }.toMutableList()
    var index = 0
    if (this.size < 2) {
        while (newList.size != 2) {
            if (index > this.size - 1) {
                index = 0
            }

            newList.add(AutoScrollItem(data = this[index]))
            index++
        }
    }
    return newList
}



suspend fun ScrollableState.autoScroll(
    dx:Float,
    animationSpec: AnimationSpec<Float> = tween(durationMillis = 800, easing = LinearEasing)
) {
    var previousValue = 0f
    scroll(MutatePriority.PreventUserInput) {
        animate(0f, dx, animationSpec = animationSpec) { currentValue, _ ->
            previousValue += scrollBy(currentValue - previousValue)
        }
    }
}



class AutoScrollItem<T>(
    val id: String = UUID.randomUUID().toString(),
    val data: T
)



/**
 * A simple grid which lays elements out vertically in evenly sized [columns].
 */
@Composable
fun VerticalGrid(
    modifier: Modifier = Modifier,
    columns: Int = 2,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val itemWidth = constraints.maxWidth / columns
        // Keep given height constraints, but set an exact width
        val itemConstraints = constraints.copy(
            minWidth = itemWidth,
            maxWidth = itemWidth
        )
        // Measure each item with these constraints
        val placeables = measurables.map { it.measure(itemConstraints) }
        // Track each columns height so we can calculate the overall height
        val columnHeights = Array(columns) { 0 }
        placeables.forEachIndexed { index, placeable ->
            val column = index % columns
            columnHeights[column] += placeable.height
        }
        val height = (columnHeights.maxOrNull() ?: constraints.minHeight)
            .coerceAtMost(constraints.maxHeight)
        layout(
            width = constraints.maxWidth,
            height = height
        ) {
            // Track the Y co-ord per column we have placed up to
            val columnY = Array(columns) { 0 }
            placeables.forEachIndexed { index, placeable ->
                val column = index % columns
                placeable.placeRelative(
                    x = column * itemWidth,
                    y = columnY[column]
                )
                columnY[column] += placeable.height
            }
        }
    }
}


@Composable
fun Loader(shimmer: Shimmer, isLoading:Boolean) {
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .shimmer(shimmer)
                .background(color = Color.LightGray)
        )
    }
}

@Composable
fun MoneyFormatter(money:String) {
    val first = money.split(',').first()
    val last = money.split(',').drop(1).joinToString()
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "$first,", style = TextStyle(fontSize = 18.sp))
        Text(text = last, style = MaterialTheme.typography.caption.copy(fontSize = 10.sp), maxLines = 2)
    }
}

fun getEmojiByUnicode(unicode:Int):String{
    var char = ""
    try {
        char = Char(unicode).toString()
    }catch (e:Exception){
        Log.e("EMOJIERROR", "getEmojiByUnicode:  ", e)
    }
    return char
}

val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
    Log.e("ADRESSFAILED", "address: ", throwable)
    CoroutineScope(Dispatchers.Main).launch {
        addressError.value = "Could Not retrieve your location"
    }
}

fun address(latitude:Double, longitude:Double, context:Context, add:(Address)->Unit){
    try {
        CoroutineScope(exceptionHandler).launch {
            val geocoder = Geocoder(context, Locale.getDefault())

            val adds = geocoder.getFromLocation(
                latitude,
                longitude,
                1
            )
            adds?.let {
                CoroutineScope(Dispatchers.Main).launch {
                    addressError.value = ""
                }
                add(it[0])
            }
        }

//        {
//
//            val address: String =
//                it[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//
//            val city: String = it[0].locality
//            val state: String = it[0].adminArea
//            val country: String = it[0].countryName
//            val postalCode: String = it[0].postalCode
//            val knownName: String = it[0].featureName // Only if available else return NULL
//
//            add("City: $city, State: $state, Country: $country, Code: $postalCode, KnownName: $knownName, Address: $address")
//        } // Here 1 represent max location result to returned, by documents it recommended 1 to 5

    }catch (e:Exception){
        Log.e("TAG", "address: ", e)
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun DeliveryInfo(modifier: Modifier = Modifier, address:com.target.supermarket.domain.models.Address? = null, borrowNumber:(PaymentNumbers)->Unit, onNegativeClick:()->Unit) {
    val viewModel:DeliveryInfoViewModel = hiltViewModel()
    address?.let {
        viewModel.setEvent(DeliveryInfoContact.InfoEvent.OnEditAddress(address))
    }
    val pagerState = rememberPagerState(initialPage = 0)
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    LaunchedEffect(key1 = Unit){
        viewModel.effect.collectLatest {
            when(it){
                is DeliveryInfoContact.InfoEffect.OnDetailsSaved -> {
                    borrowNumber(it.numbers)
                    onNegativeClick()
                }
                DeliveryInfoContact.InfoEffect.OnFirstPartOK -> {
                    scope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                }
                DeliveryInfoContact.InfoEffect.OnSecondPartOK -> {
                    viewModel.setEvent(DeliveryInfoContact.InfoEvent.OnSaveDeliveryInfo)
                    onNegativeClick()
                }
            }
        }
    }
    LaunchedEffect(key1 = viewModel.state.value.district){
        viewModel.setEvent(DeliveryInfoContact.InfoEvent.OnGetDistricts)
    }
    Column(horizontalAlignment = Alignment.End, modifier = modifier
        .fillMaxWidth()
        .padding(16.dp))
    {
        Text(text = "Delivery Information", style = MaterialTheme.typography.h6.copy(fontSize = 16.sp, textAlign = TextAlign.Center), modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        TabRow(selectedTabIndex = pagerState.currentPage, backgroundColor = Color.Transparent, indicator = {
            TabRowDefaults.Indicator(color = Color.Transparent)
        })
        {
            Tab(modifier = Modifier.padding(bottom = 8.dp), selected = pagerState.currentPage==0, onClick = { }) {
                Icon(painter = painterResource(id = R.drawable.person), modifier = Modifier.size(40.dp), contentDescription = "", tint = if (pagerState.currentPage == 0) lightBlue else Color.LightGray)
            }
            Tab(modifier = Modifier.padding(bottom = 8.dp), selected = pagerState.currentPage==1, onClick = { }) {
                Icon(painter = painterResource(id = R.drawable.location), modifier = Modifier.size(40.dp), contentDescription = "", tint = if (pagerState.currentPage == 1) lightBlue else Color.LightGray)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalPager(state = pagerState, modifier = Modifier.wrapContentSize(), count = 2) {position->
            when(position){
                0-> Column(Modifier.wrapContentHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
                        TransparentInput(
                            value = viewModel.state.value.names,
                            label = "Full Names",
                            keyboardActions = KeyboardActions(onNext = {focusManager.moveFocus(
                                FocusDirection.Down)}),
                            isError = viewModel.state.value.nameError.isNotEmpty()
                        ){
                            viewModel.setEvent(DeliveryInfoContact.InfoEvent.OnNamesChanged(it))
                        }
                        TransparentInput(value = viewModel.state.value.email, label = "Email",
                            keyboardActions = KeyboardActions(onNext = {focusManager.moveFocus(
                                FocusDirection.Down)}),
                            isError = viewModel.state.value.emailError.isNotEmpty()
                        ){
                            viewModel.setEvent(DeliveryInfoContact.InfoEvent.OnEmailChanged(it.trim()))
                        }
                        TransparentInput(value = viewModel.state.value.contact, label = "Contact",keyboardActions = KeyboardActions(onDone = {
                            focusManager.clearFocus()
                            viewModel.setEvent(DeliveryInfoContact.InfoEvent.CheckForErrors)
                        }), keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Phone),
                            isError = viewModel.state.value.contactError.isNotEmpty()){
                            viewModel.setEvent(DeliveryInfoContact.InfoEvent.OnContactChanged(it.trim()))
                        }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = viewModel.state.value.infoError, style = MaterialTheme.typography.caption.copy(color = errorColor))
                    }
                1-> LazyColumn(modifier = Modifier
                    .wrapContentHeight()
                    .padding(bottom = 16.dp))
                    {
                        item {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                ExposedDropdown(
                                    items = viewModel.state.value.districts.map {
                                        Pair(
                                            it.name,
                                            it.isActive
                                        )
                                    },
                                    onSelected = {
                                        viewModel.setEvent(
                                            DeliveryInfoContact.InfoEvent.OnDistrictChanged(
                                                it
                                            )
                                        )
                                    },
                                    value = viewModel.state.value.district?.name ?: "",
                                    label = "District",
                                    keyboardActions = KeyboardActions(onNext = {
                                        focusManager.moveFocus(
                                            FocusDirection.Down
                                        )
                                    }),
                                    isError = viewModel.state.value.districtError.isNotEmpty()
                                ) {}

                                ExposedDropdown(
                                    items = viewModel.state.value.divisions.map {
                                        Pair(
                                            it.name,
                                            it.isActive
                                        )
                                    },
                                    onSelected = {
                                        viewModel.setEvent(
                                            DeliveryInfoContact.InfoEvent.OnDivisionChanged(
                                                it
                                            )
                                        )
                                    },
                                    value = viewModel.state.value.division?.name ?: "",
                                    label = "Division",
                                    keyboardActions = KeyboardActions(onNext = {
                                        focusManager.moveFocus(
                                            FocusDirection.Down
                                        )
                                    }),
                                    isError = viewModel.state.value.divisionError.isNotEmpty()
                                ) { }

                                ExposedDropdown(
                                    items = viewModel.state.value.parishes.map {
                                        Pair(
                                            it.name,
                                            it.isActive
                                        )
                                    },
                                    onSelected = {
                                        viewModel.setEvent(
                                            DeliveryInfoContact.InfoEvent.OnParishChanged(
                                                it
                                            )
                                        )
                                    },
                                    value = viewModel.state.value.parish?.name ?: "",
                                    label = "Parish",
                                    keyboardActions = KeyboardActions(onNext = {
                                        focusManager.moveFocus(
                                            FocusDirection.Down
                                        )
                                    }),
                                    isError = viewModel.state.value.parishError.isNotEmpty()
                                ) {
//                                    viewModel.setEvent(
//                                        DeliveryInfoContact.InfoEvent.OnFetchAddress(
//                                            "parish",
//                                            viewModel.state.value.division?.id ?: 0
//                                        )
//                                    )
                                }
                                ExposedDropdown(
                                    items = viewModel.state.value.villages.map {
                                        Pair(
                                            it.name,
                                            it.isActive
                                        )
                                    },
                                    onSelected = {
                                        viewModel.setEvent(
                                            DeliveryInfoContact.InfoEvent.OnVillageChanged(
                                                it
                                            )
                                        )
                                    },
                                    value = viewModel.state.value.village?.name ?: "",
                                    label = "Village",
                                    keyboardActions = KeyboardActions(onNext = {
                                        focusManager.moveFocus(
                                            FocusDirection.Down
                                        )
                                    }),
                                    isError = viewModel.state.value.villageError.isNotEmpty()
                                ) {
//                                    viewModel.setEvent(
//                                        DeliveryInfoContact.InfoEvent.OnFetchAddress(
//                                            "village",
//                                            viewModel.state.value.parish?.id ?: 0
//                                        )
//                                    )
                                }
                                ExposedDropdown(
                                    items = viewModel.state.value.streets.map {
                                        Pair(
                                            it.name,
                                            it.isActive
                                        )
                                    },
                                    onSelected = {
                                        viewModel.setEvent(
                                            DeliveryInfoContact.InfoEvent.OnStreetChanged(
                                                it
                                            )
                                        )
                                    },
                                    value = viewModel.state.value.street?.name ?: "",
                                    label = "Street/Road",
                                    keyboardActions = KeyboardActions(onNext = {
                                        focusManager.moveFocus(
                                            FocusDirection.Down
                                        )
                                    }),
                                    isError = viewModel.state.value.streetError.isNotEmpty()
                                ) {
//                                    viewModel.setEvent(
//                                        DeliveryInfoContact.InfoEvent.OnFetchAddress(
//                                            "street",
//                                            viewModel.state.value.village?.id ?: 0
//                                        )
//                                    )
                                }

                                TransparentInput(value = viewModel.state.value.roomNo, label = "House/Room No (optional)",
                                    keyboardActions = KeyboardActions(onNext = {focusManager.clearFocus(true)})
                                    , keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Phone)
                                ){
                                    viewModel.setEvent(DeliveryInfoContact.InfoEvent.OnRoomNoChanged(it))
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(text = viewModel.state.value.infoError, style = MaterialTheme.typography.caption.copy(color = errorColor))
                            }
                        }
                    }
            }
        }

        Row(modifier = Modifier
            .padding(16.dp), verticalAlignment = Alignment.CenterVertically)
        {
            TextButton(onClick = {
                if (pagerState.currentPage==0){
                    onNegativeClick()
                }else {
                    scope.launch {
                        pagerState.animateScrollToPage(0)
                    }
                }
            }) {
                Text(text = if (pagerState.currentPage==0) "Cancel" else "Previous")
            }
            TextButtonCustom(title = if (pagerState.currentPage==0) "Next" else "Save") {
                if (pagerState.currentPage==0){
                    viewModel.setEvent(DeliveryInfoContact.InfoEvent.CheckForErrors)
                }else{
                    viewModel.setEvent(DeliveryInfoContact.InfoEvent.CheckForAdErrors)
                }
            }
        }
    }
}


@Composable
fun FavIcon(modifier: Modifier, imageVector: ImageVector, onClick: () -> Unit) {
    IconButton(onClick = { onClick() }, modifier = modifier) {
        Icon(imageVector = imageVector, contentDescription = "", Modifier.size(24.dp), tint = Color.Gray)
    }
}

@Composable
fun FavIconInt(modifier: Modifier, res: Int , onClick: () -> Unit) {
    IconButton(onClick = { onClick() }, modifier = modifier) {
        Icon(painter = painterResource(id = res), contentDescription = "", Modifier.size(24.dp), tint = Color.Gray)
    }
}

@Composable
fun CartQtyBtn(modifier: Modifier,qty:Int, context: Context, p:LocalProduct) {
    Row(modifier.wrapContentWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround) {
        AddButton(icon = painterResource(id = R.drawable.plus)) {CommonMethods.toggleCart(context,true, p)}
        Text(text = "$qty", style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold, color = lightBlue), modifier = Modifier.padding(horizontal = 16.dp))
        AddButton(icon = painterResource(id = R.drawable.dash)) { CommonMethods.toggleCart(context,false, p) }
    }
}

@Composable
fun CartAddButton(modifier: Modifier = Modifier, context:Context, p:LocalProduct) {
    if (p.qtyInCart==0) {
        IconButton(onClick = {
            CommonMethods.toggleCart(context, true, p)
        }, modifier = modifier) {
            Icon(
                painter = painterResource(id = R.drawable.cart_add),
                contentDescription = "",
                modifier = Modifier.size(25.dp),
                tint = lightBlue
            )
        }
    }else{
        IconButton(onClick = {
            CommonMethods.toggleCart(context, false, p)
        }, modifier = modifier) {
            Icon(
                painter = painterResource(id = R.drawable.cart_close),
                contentDescription = "",
                modifier = Modifier.size(25.dp),
                tint = lightBlue
            )
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CartHolder(modifier: Modifier = Modifier, sheetState: ModalBottomSheetState,scope: CoroutineScope, viewModel: CommonViewModel) {
    Surface(shape = CircleShape, modifier = modifier
        .height(60.dp)
        .wrapContentWidth()
        .padding(start = 32.dp)) {
        Box(modifier = Modifier
            .fillMaxHeight()
            .background(color = lightBlue)
            .padding(8.dp)
        ) {
//            if (expanded)
//                IconButton(onClick = { expanded = !expanded }, modifier = Modifier
//                    .padding(start = 8.dp)
//                    .align(Alignment.CenterStart)) {
//                    Icon(painter = painterResource(id = R.drawable.visibility), contentDescription = "", tint = Color.White, modifier = Modifier.size(16.dp))
//                }

            IconButton(onClick = {
                scope.launch {
                    if (sheetState.isVisible) sheetState.hide()
                    else sheetState.show()
                }
            }, modifier = Modifier.align(
                Alignment.CenterEnd)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(painter = painterResource(id = R.drawable.basket), contentDescription = "", tint = Color.White)
                    Text(text = "${viewModel.state.value.cartItems.count()}", style = MaterialTheme.typography.h6.copy(color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold), modifier = Modifier.padding(top = 12.dp))
                }
            }

//            LazyRow(modifier = Modifier
//                .fillMaxWidth()
//                .padding(end = 65.dp, start = 65.dp), reverseLayout = true, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End){
//                items(viewModel.state.value.cartItems){prod->
//                    val image = getImage(url = prod.image, onSuccess = {}){
//
//                    }
//                    Image(painter = image, contentDescription = "", modifier = Modifier.size(50.dp))
//                }
//            }
        }
    }
}


@Composable
fun IndicatorDot(
    modifier: Modifier = Modifier,
    size: Dp,
    color: Color
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(color)
    )
}

@Composable
fun DotsIndicator(
    modifier: Modifier = Modifier,
    totalDots: Int,
    selectedIndex: Int,
    selectedColor: Color = lightBlue /* Color.Yellow */,
    unSelectedColor: Color = lighterGray /* Color.Gray */,
    dotSize: Dp
) {
    LazyRow(
        modifier = modifier
            .wrapContentWidth()
            .wrapContentHeight()
    ) {
        items(totalDots) { index ->
            IndicatorDot(
                color = if (index == selectedIndex) selectedColor else unSelectedColor,
                size = dotSize
            )

            if (index != totalDots - 1) {
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            }
        }
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun AutoSlidingCarousel(
    modifier: Modifier = Modifier,
    autoSlideDuration: Long = 3000L,
    pagerState: PagerState = remember { PagerState() },
    itemsCount: Int,
    itemContent: @Composable (index: Int) -> Unit,
) {
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()

    LaunchedEffect(pagerState.currentPage) {
        try {
            delay(autoSlideDuration)
            pagerState.animateScrollToPage((pagerState.currentPage + 1) % itemsCount)
        }catch (e:Exception){
            Log.e("TAG", "AutoSlidingCarousel: ", )
        }
    }

    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        HorizontalPager(count = itemsCount, state = pagerState) { page ->
            itemContent(page)
        }

        // you can remove the surface in case you don't want
        // the transparant bacground
        Surface(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .align(Alignment.TopCenter),
            shape = CircleShape,
            color = Color.Black.copy(alpha = 0.5f)
        ) {
            DotsIndicator(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                totalDots = itemsCount,
                selectedIndex = if (isDragged) pagerState.currentPage else pagerState.targetPage,
                dotSize = 8.dp
            )
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CartPReview(modifier: Modifier = Modifier, sheetState:ModalBottomSheetState, scope: CoroutineScope, viewModel: CommonViewModel, onCheckOut:()->Unit, content: @Composable () -> Unit) {
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
                       Box(modifier = Modifier) {
                           VerticalGrid(columns = 3, modifier = Modifier.padding(vertical = 50.dp)) {
                               viewModel.state.value.cartItems.forEach {item->
                                   val image = getImage(url = item.image, onSuccess = {}){

                                   }
                                   Image(painter = image, contentDescription = "", modifier = Modifier
                                       .size(50.dp)
                                       .padding(vertical = 8.dp))
                               }
                           }
                           TextButtonCustom(title = "Check Out", modifier = Modifier
                               .align(
                                   Alignment.BottomEnd
                               )
                               .padding(end = 8.dp, bottom = 8.dp)) {
                               scope.launch {
                                   sheetState.hide()
                                   onCheckOut()
                               }
                           }
                           IconButton(onClick = { scope.launch { sheetState.hide() } },modifier = Modifier.align(
                                   Alignment.TopEnd)) {
                               Icon(painter = painterResource(id = R.drawable.close_circle), contentDescription = "", modifier = Modifier.size(25.dp), tint = lightBlue)
                           }

                           Text(text = "Your Selection Preview", style = MaterialTheme.typography.h6, modifier = Modifier
                               .align(
                                   Alignment.TopStart
                               )
                               .padding(12.dp))
                       }
        },
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        content()
    }
}

@Composable
fun Cartable(modifier: Modifier = Modifier, product: LocalProduct,content:@Composable ()->Unit) {
    val context = LocalContext.current
    Box(modifier = modifier) {
        content()
        CartAddButton(context = context, p = product, modifier = Modifier.align(Alignment.BottomEnd))
    }
}


