package com.bootcamp.watch

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MealListActivity : AppCompatActivity(), MealListAdapter.Callback {
  private var adapter: MealListAdapter? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    val meals = MealStore.fetchMeals(this)
    adapter = MealListAdapter(meals, this)
    list.adapter = adapter
    list.layoutManager = LinearLayoutManager(this)
  }

  override fun mealClicked(meal: Meal) {
    // TODO: Handle this meal click!
  }
}
