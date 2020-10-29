package com.teamarche.recipemanager.data

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.teamarche.recipemanager.model.Recipe
import com.teamarche.recipemanager.model.RecipeList
import java.io.File
import java.nio.file.AccessMode
import java.util.stream.Collectors


//Manages loading/storing of data from/to persistent storage
class Repository(val context : Context) {

    val gson = Gson();

    // Load all recipes from storage
    fun loadAll() : RecipeList {
        val listOfRecipes   = ArrayList<Recipe>()
        val files           = context.fileList()
        for (fileName in files) {
            val fin = context.openFileInput(fileName)
            //in case JSON is in multiline format
            listOfRecipes.add(gson.fromJson(fin.bufferedReader()
                .lines()
                .toArray()
                .joinToString("\n"),
                Recipe::class.java))
            fin.close()
        }
        Log.d("repository", "Loaded ${listOfRecipes.size} recipes from internal storage")
        return RecipeList(listOfRecipes)
    }

    //Store a new recipe as long as there are no naming conflicts
    fun storeNew(r : Recipe) {
        if (!(r.title in context.fileList())) {
            val fos =  context.openFileOutput(r.title, Context.MODE_PRIVATE)
            fos.writer().run {
                write(gson.toJson(r))
                flush()
                close()
            }
            Log.d("repository", "Wrote recipe ${r.title} to internal storage")
        }
    }

    //Update the content of a file, assuming the recipe retained its title
    fun update(r : Recipe) {
        //delete and create new
        context.deleteFile(r.title)
        storeNew(r)
    }

    //Store all recipes
    //TODO: add updated flags to prevent unnecessary delete/write operations
    fun storeAll(recipeList : RecipeList) {
        for (r in recipeList) {
            update(r)
        }
    }

    //Rename a file, likely invoked because a recipe title was changed
    fun rename(old : String, r : Recipe) {
       context.deleteFile(old)
        storeNew(r)

    }

    //Delete a recipe
    fun delete(r : Recipe) {
        context.deleteFile(r.title)
    }

    //Delete all recipes
    fun deleteAll() {
        for (fileName in context.fileList()) {
            context.deleteFile(fileName)
        }
    }

    companion object {
        lateinit var repo : Repository
    }

}