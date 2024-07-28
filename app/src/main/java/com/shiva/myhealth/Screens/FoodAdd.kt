package com.shiva.myhealth.Screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.shiva.myhealth.R
import com.shiva.myhealth.data.FoodTimeType
import com.shiva.myhealth.data.Product
import com.shiva.myhealth.data.ProductType
import com.shiva.myhealth.models.DiaryViewModel
import com.shiva.myhealth.models.FoodAddViewModel
import com.shiva.myhealth.ui.theme.MyHealthTheme
import com.shiva.myhealth.utility.parseFloat
import com.shiva.myhealth.utility.parseInt
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@Composable
fun FoodAdd(
    eatingType: String?,
    modifier: Modifier,
    modelDiary: DiaryViewModel = hiltViewModel(),
    model: FoodAddViewModel = hiltViewModel()
) {

    val selectedProductType by model.selectedTypeProduct.collectAsState()
    val eatingFoodTime by model.eatingFoodTime.collectAsState()

    model.getEatingTimeName(eatingType)
    model.getEatingFoodTime(modelDiary, eatingType)

    if (model.foodAddDialog) {
        FoodDetailDialog(
            showDialog = model::foodAddDialogShow,
            selectedProductType,
            model::addProduct,
            model::editProduct
        )
    }
    if (model.foodEditDialog) {
        FoodDetailDialog(
            showDialog = model::foodEditDialogShow,
            selectedProductType,
            model::addProduct,
            model::editProduct,
            true
        )
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        //TODO выбор времени?
        Text(
            text = stringResource(model.eatingTimeName),
            style = MaterialTheme.typography.headlineMedium
        )

        FoodSection(Modifier, model::onProductItemSelected, eatingFoodTime.products)

        //TODO список выбранных продуктов
        FoodDetailList(model.products, model::onEditSwipe, model::onDelSwipe)
    }
}

@Composable
fun FoodSection(
    modifier: Modifier = Modifier,
    onProductItemSelected: (Product) -> Unit,
    products: MutableList<Product>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3), modifier = modifier.padding(8.dp).background(
            color = MaterialTheme.colorScheme.secondaryContainer.copy(
                alpha = .50f
            ), shape = RoundedCornerShape(8.dp)
        ), horizontalArrangement = Arrangement.Center
    ) {
        listOf(
            Product.Eggs,
            Product.Soup,
            Product.Fish,
            Product.Meat,
            Product.Bakery,
            Product.Candies,
            Product.Cheese,
            Product.Fruts,
            Product.Porridge,
            Product.Snack,
            Product.Vegetables,
            Product.Water,
            Product.OtherFood,
        ).forEach { productList ->
            item {

                Box(contentAlignment = Alignment.TopEnd) {
                    val count = products.count { it.productCategory == productList }
                    FoodSectionItem(productList, onProductItemSelected, count)
                    if (count != 0) {
                        Text(
                            count.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.size(27.dp).padding(top = 6.dp, end = 6.dp)
                                .border(1.dp, color = Color.Black, CircleShape).clip(CircleShape)
                                .background(
                                    color = MaterialTheme.colorScheme.secondaryContainer.copy(
                                        0.5f
                                    )
                                )
                        )
                    }
                }

            }

        }
    }
}

@Composable
fun FoodSectionItem(
    productType: ProductType,
    onProductItemSelected: (Product) -> Unit,
    count: Int
) {
    val color = if (count > 0) Color.Green.copy(alpha = 0.8f) else Color.Transparent
    Column(
        modifier = Modifier.fillMaxWidth().padding(4.dp)
            .border(2.dp, color = Color.Black, RoundedCornerShape(8.dp)).background(
                color = color, shape = RoundedCornerShape(8.dp)
            ).clip(RoundedCornerShape(8.dp))
            .clickable { onProductItemSelected(Product(productCategory = productType)) },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Icon(
            productType.icon,
            stringResource(productType.name),
            modifier = Modifier.padding(top = 4.dp)
        )

        Text(
            stringResource(productType.name), modifier = Modifier, textAlign = TextAlign.Center
        )
    }
}

@Composable
fun FoodDetailList(
    products: SnapshotStateList<Product>,
    onEditSwipe: (Product) -> Unit,
    onDelSwipe: (Product) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(8.dp).fillMaxWidth().background(
            color = MaterialTheme.colorScheme.secondaryContainer.copy(
                alpha = .50f
            ), shape = RoundedCornerShape(8.dp)
        )
    ) {
        items(products) {

            val delete = SwipeAction(onSwipe = {
                onDelSwipe(it)
            }, icon = {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete product",
                    modifier = Modifier.padding(16.dp),
                    tint = Color.White
                )
            }, background = Color.Red.copy(alpha = 0.5f), isUndo = true
            )
            val archive = SwipeAction(onSwipe = {
                onEditSwipe(it)
            }, icon = {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "edit product",
                    modifier = Modifier.padding(16.dp),

                    tint = Color.White

                )
            }, background = Color(0xFF50B384).copy(alpha = 0.7f)
            )
            SwipeableActionsBox(
                modifier = Modifier,
                swipeThreshold = 200.dp,
                startActions = listOf(archive),
                endActions = listOf(delete)
            ) {
                FoodDetailListItem(it)
            }
        }
    }
}

@Composable
fun FoodDetailListItem(product: Product) {
    Column(
        modifier = Modifier.padding(8.dp).fillMaxWidth().background(
            color = MaterialTheme.colorScheme.secondaryContainer.copy(
                alpha = .50f
            ), shape = RoundedCornerShape(8.dp)
        ), horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(/*verticalAlignment = Alignment.,*/
            modifier = Modifier.padding(4.dp)
        ) {
            Icon(product.productCategory.icon, stringResource(product.productCategory.name))
            Text(
                stringResource(product.productCategory.name),
                Modifier.padding(end = 4.dp),
                style = MaterialTheme.typography.labelLarge,

                )
            Text(
                "${(product.gramms.toFloat() / 100) * product.caloriesPer100Gramms} калл. в ${product.gramms} гр",
                style = MaterialTheme.typography.labelLarge
            )
        }
        OutlinedTextField(product.description,
            { product.description = it },
            Modifier.fillMaxWidth().padding(start = 8.dp, bottom = 8.dp, end = 8.dp),
            enabled = false,
            label = { Text(stringResource(R.string.description)) })
    }
}

@Composable
fun FoodDetailDialog(
    showDialog: (Boolean) -> Unit,
    product: Product,
    onAcceptProduct: (Product) -> Unit,
    editProduct: (Product) -> Unit,
    isEdit: Boolean = false
) {

    var caloriesPer100Gramms = remember { mutableIntStateOf(product.caloriesPer100Gramms) }
    var caloriesSummery = remember { mutableFloatStateOf(product.caloriesSummery) }
    var gramms = remember { mutableIntStateOf(product.gramms) }
    var description = remember { mutableStateOf(product.description) }
    val toastShow = remember { mutableStateOf(false) }
    Dialog(
        onDismissRequest = { showDialog(false) }, DialogProperties(
            dismissOnBackPress = false, dismissOnClickOutside = true
        )
    ) {
        if (toastShow.value) {
            Toast.makeText(LocalContext.current, R.string.toast_add, Toast.LENGTH_LONG).show()
            toastShow.value = false
        }
        Surface(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(product.productCategory.icon, "")
                Text(stringResource(product.productCategory.name))


                OutlinedTextField(caloriesPer100Gramms.intValue.toString(),
                    {
                        if (it != "") {

                            caloriesPer100Gramms.intValue = it.parseInt(it)
                            caloriesSummery.floatValue =
                                ((caloriesPer100Gramms.intValue.toFloat() / 100 * gramms.intValue))
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    leadingIcon = { Icon(Icons.Default.MenuBook, "") },
                    label = { Text(stringResource(R.string.calories_100_gr)) }) // калорий на 100 гр

                OutlinedTextField(gramms.intValue.toString(),
                    {
                        if (it != "") {
                            gramms.intValue = it.parseInt(it)
                            caloriesSummery.floatValue =
                                ((caloriesPer100Gramms.intValue.toFloat() / 100 * gramms.intValue))
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    leadingIcon = { Icon(Icons.Default.Scale, "") },
                    label = { Text(stringResource(R.string.gramm)) }) //грамм

                OutlinedTextField(caloriesSummery.floatValue.toString(),
                    { caloriesSummery.floatValue = it.parseFloat(it) },
                    enabled = false,
                    leadingIcon = { Icon(Icons.Default.LocalDining, "") },
                    label = { Text(stringResource(R.string.calories_summary)) }) //общие калории


                OutlinedTextField(description.value,
                    { description.value = it },
                    minLines = 2,
                    maxLines = 3,
                    label = { Text(stringResource(R.string.description)) }) //описание


                Button({
                    if (caloriesPer100Gramms.intValue != 0 && gramms.intValue != 0 && !isEdit) onAcceptProduct(
                        Product(
                            productCategory = product.productCategory,
                            caloriesPer100Gramms.intValue,
                            caloriesSummery.floatValue,
                            gramms.intValue,
                            description.value
                        )
                    )
                    else if (caloriesPer100Gramms.intValue != 0 && gramms.intValue != 0 && isEdit) {
                        editProduct(
                            Product(
                                productCategory = product.productCategory,
                                caloriesPer100Gramms.intValue,
                                caloriesSummery.floatValue,
                                gramms.intValue,
                                description.value
                            )
                        )
                    } else toastShow.value = true
                }, Modifier) {
                    Text(stringResource(R.string.add_product_btn))
                }

                Button({ showDialog(false) }, Modifier) {
                    Text(stringResource(R.string.cancel_btn))
                }

            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun FoodAddPreview() {
    MyHealthTheme {
        FoodAdd(FoodTimeType.Breakfast.n, modelDiary = DiaryViewModel(), modifier = Modifier)
    }
}