package com.teamarche.recipemanager.model

import com.teamarche.recipemanager.data.Repository


//A wrapper for all recipes saved by the app
class RecipeList(val items : MutableList<Recipe>  = ArrayList())  : Iterable<Recipe> by items{

    //Add a recipe to the list and store it
    fun add(r : Recipe) : Int {
        if (!items.any{it.title == r.title}) {
            items.add(r)
            Repository.repo.storeNew(r)
            return items.size - 1
        } else {
            return -1
        }
    }

    //Remove a recipe from the list
    fun remove(r : Recipe) {
        if (r in items) {
            items.remove(r)
            Repository.repo.delete(r)
        }
    }

    fun removeAt(i : Int) {
        remove(items[i])
    }

    //Retunrs the object count
    fun size() = items.size

    //Returns the item at the specified index
    operator fun get(i : Int) : Recipe {
        return items[i]
    }

}