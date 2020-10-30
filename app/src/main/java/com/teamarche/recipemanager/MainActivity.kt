package com.teamarche.recipemanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.internal.`$Gson$Types`
import com.teamarche.recipemanager.data.Repository
import com.teamarche.recipemanager.model.Ingredient
import com.teamarche.recipemanager.model.Recipe
import com.teamarche.recipemanager.model.RecipeList

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.recipe_list_item.*
import kotlinx.android.synthetic.main.recipe_list_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    //LayoutManager and ViewAdapter for recipe list
    private lateinit var recipeListAdapter : RecyclerView.Adapter<*>
    private lateinit var recipeListManager : RecyclerView.LayoutManager

    companion object {
        lateinit var recipeList : RecipeList
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //set up repository
        Repository.repo = Repository(applicationContext)
        recipeList = Repository.repo.loadAll()


        recipeListManager = LinearLayoutManager(this)
        recipeListAdapter = RecipeListAdapter(recipeList)

        recipe_list.apply {
            //dimensions of list do not change when content does
            setHasFixedSize(true)
            adapter = recipeListAdapter
            layoutManager = recipeListManager
        }

        add_recipe_fab.setOnClickListener { view ->
            //Adding and Editing a Recipe use the same Activity
            val intent = Intent(this, EditRecipeActivity::class.java).apply {
                putExtra("isEditMode", false)
                putExtra("existingRecipeIndex", "")
            }
            startActivity(intent)
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



    //Custom ViewAdapter for Recycler View
    class RecipeListAdapter(private val recipes : RecipeList)
        : RecyclerView.Adapter<RecipeListAdapter.RecipeListItemHolder>() {

        //ViewHolder holds a RelativeLayout object
        class RecipeListItemHolder(val relay : RelativeLayout)
            : RecyclerView.ViewHolder(relay)

        //Creates RecipeListItemHolder without setting its content
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeListItemHolder {
            val relLayout = LayoutInflater.from(parent.context).inflate(R.layout.recipe_list_item, parent, false)
                    as RelativeLayout
            return RecipeListItemHolder(relLayout)
        }

        //Binds a RecipeListItemholder to its corresponding content determined by its position in the dataset
        override fun onBindViewHolder(holder: RecipeListItemHolder, position: Int) {
            holder.relay.recipe_list_item_title.text = recipes[position].title
            val sdf = SimpleDateFormat("dd-MM-yyyy")
            holder.relay.recipe_list_item_date.text = "Last Cooked: " + (Date().time -recipes[position].lastCooked.time)/ (1000 * 60 * 60 * 24) + " days ago"
            holder.relay.recipe_list_item_favorite_btn.isChecked = recipes[position].isFavorite

            holder.relay.recipe_list_item_favorite_btn.setOnCheckedChangeListener{ btn, checked ->
                recipes[position].isFavorite = checked
            }

            holder.relay.recipe_list_item_remove_btn.setOnClickListener { view ->
                recipeList.removeAt(position)
                this.notifyItemRemoved(position)
            }
        }

        override fun getItemCount() = recipes.size()
    }
}
