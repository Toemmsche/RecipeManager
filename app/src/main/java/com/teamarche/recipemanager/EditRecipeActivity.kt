package com.teamarche.recipemanager

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.children
import androidx.core.view.get
import com.teamarche.recipemanager.model.Ingredient
import com.teamarche.recipemanager.model.Recipe
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.add_ingredient.*
import kotlinx.android.synthetic.main.content_edit.*
import java.time.Duration
import java.util.*
import kotlin.collections.ArrayList

class EditRecipeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        add_ingredient_btn.setOnClickListener { view ->
            // A new view is appended to the layout
            LinearLayout.inflate(this, R.layout.add_ingredient, ingredient_list_layout)
        }

        submit_recipe_btn.setOnClickListener { view ->
            val title = recipe_edit_title.text.toString()
            val duration = Duration.ofMinutes(recipe_edit_duration.text.toString().toLong())
            val link = recipe_edit_link.text.toString()
            val IngredientList = ArrayList<Ingredient>()
            for(child in ingredient_list_layout.children) {
                val et =  child as LinearLayout

                //cast to EditText and extract
                IngredientList.add(
                    Ingredient(
                        (et[0] as EditText).text.toString(),
                        (et[1] as EditText).text.toString().toInt(),
                        (et[2] as EditText).text.toString()
                    )
                )
            }
            val r = Recipe(title, duration, IngredientList, link)
            if(checkRecipe(r)) {
                MainActivity.recipeList.add(r)
                finish()
            } else {
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("The recipe is invalid. Please enter legitimate values")
                    .setCancelable(true)
                    .show()
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    //Validates the Recipe to ensure non-empty strings and valid URLs
    fun checkRecipe(r : Recipe) : Boolean {
        return !r.title.isNullOrBlank() && !r.duration.isNegative && !r.ingredients.any {
            it.name.isNullOrBlank() || it.quantity <= 0 || it.unit.isNullOrBlank()
        }
    }
}