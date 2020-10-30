package com.teamarche.recipemanager.model

//A single, measurable ingredient
class Ingredient(
    var name : String,
    var quantity : Int = 1,
    var unit : String = "unit" ) {
}