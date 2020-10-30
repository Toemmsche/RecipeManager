package com.teamarche.recipemanager.model

import android.content.Context
import com.teamarche.recipemanager.data.Repository
import java.time.Duration
import java.util.*
import kotlin.collections.ArrayList

//A single recipe
class Recipe  constructor(
    titleField : String,
    var duration : Duration = Duration.ofMinutes(0),
    var ingredients : MutableList<Ingredient> = ArrayList(),
    var link : String = "",
    var addedOn : Date = Date(),
    var lastCooked : Date = Date(),
    var isFavorite : Boolean = false
) {

    var title = titleField
        private set
    //Rename a recipe
    fun rename(newName : String) {
        var old = title
        title = newName
        Repository.repo.rename(old, this)
    }

    companion object {
        val singleton  = Recipe("Carbonara",
            Duration.ofMinutes(30),
            mutableListOf(Ingredient.singleton),
        "chefkoch.de",
            Date(),
            Date(),
            false)
    }

    override fun equals(other: Any?): Boolean {
        if (other is Recipe) {
            return other.title == this.title
        } else {
            return false
        }
    }
}