package com.shiva.myhealth.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BakeryDining
import androidx.compose.material.icons.filled.EggAlt
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Icecream
import androidx.compose.material.icons.filled.KebabDining
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.RiceBowl
import androidx.compose.material.icons.filled.SetMeal
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.filled.SoupKitchen
import androidx.compose.material.icons.filled.SportsRugby
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.ui.graphics.vector.ImageVector
import com.shiva.myhealth.R

data class Product(
    val productCategory: ProductType = Bakery,
    val caloriesPer100Gramms: Int = 0,
    val caloriesSummery: Float = 0f,
    var gramms: Int = 0,
    var description: String = "",
) {
    data object Bakery : ProductType(
        icon = Icons.Default.BakeryDining,
        name = R.string.bakery,
    )

    data object Porridge : ProductType(
        icon = Icons.Default.RiceBowl,
        name = R.string.porridge,
    )

    data object Soup : ProductType(
        icon = Icons.Default.SoupKitchen,
        name = R.string.soup,
    )

    data object Eggs : ProductType(
        icon = Icons.Default.EggAlt,
        name = R.string.eggs,
    )

    data object Meat : ProductType(
        icon = Icons.Default.KebabDining,
        name = R.string.meat,
    )

    data object Cheese : ProductType(
        icon = Icons.Default.PieChart,
        name = R.string.cheese,
    )

    data object Fruts : ProductType(
        icon = Icons.Default.SportsRugby,
        name = R.string.fruts,
    )

    data object Vegetables : ProductType(
        icon = Icons.Default.ShoppingBasket,
        name = R.string.vegetables,
    )

    data object Candies : ProductType(
        icon = Icons.Default.Icecream,
        name = R.string.candies,
    )

    data object Fish : ProductType(
        icon = Icons.Default.SetMeal,
        name = R.string.fish,
    )

    data object Snack : ProductType(
        icon = Icons.Default.Fastfood,
        name = R.string.snack,
    )

    data object OtherFood : ProductType(
        icon = Icons.Default.Restaurant,
        name = R.string.other_food,
    )

    data object Water : ProductType(
        icon = Icons.Default.WaterDrop,
        name = R.string.water,
    )
}

sealed class ProductType(
    val icon: ImageVector,
    val name: Int,
)