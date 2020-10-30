package com.teamarche.recipemanager

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.teamarche.recipemanager.data.Repository
import com.teamarche.recipemanager.model.Ingredient
import com.teamarche.recipemanager.model.Recipe
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.BeforeClass
import java.lang.annotation.RetentionPolicy
import java.time.Duration
import java.util.*

/**
 * Test of the Repopsitory IO
 */
@RunWith(AndroidJUnit4::class)
class RepositoryTest {
    val dummyRecipe = Recipe("test")


    companion object {
        @BeforeClass
        @JvmStatic
        fun init() {
            val appContext = InstrumentationRegistry.getInstrumentation().targetContext
            Repository.repo = Repository(appContext)
            Repository.repo.deleteAll()
        }
    }

    @Test
    fun testStoringAndLoading() {
        Repository.repo.storeNew(dummyRecipe)
        Log.d("repotest", Repository.repo.loadAll().first().title)
        assertTrue("Storing or Loading operation failed",Repository.repo.loadAll().contains(dummyRecipe))
    }


    @Test
    fun testRename() {
        Repository.repo.storeNew(dummyRecipe)
        val newName = "notADummyRecipe"
        dummyRecipe.rename(newName)
        Log.d("repository", Repository.repo.loadAll().first().title)
        assertTrue("Renaming failed", Repository.repo.loadAll().contains(Recipe(newName)))
        Recipe(newName).rename(dummyRecipe.title)
        assertTrue("Renaming failed", Repository.repo.loadAll().contains(dummyRecipe))
    }

    @Test
    fun testDelete() {
        Repository.repo.storeNew(dummyRecipe)
        Repository.repo.delete(dummyRecipe)
        assertFalse("Deletion failed", Repository.repo.loadAll().contains(dummyRecipe))
    }

    @Test
    @After
    fun cleanDirectory() {
        Repository.repo.deleteAll()
    }

}
