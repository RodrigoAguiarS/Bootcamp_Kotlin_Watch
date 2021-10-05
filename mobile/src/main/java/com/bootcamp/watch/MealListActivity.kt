package com.bootcamp.watch

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.rodrigo.shared.Meal
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*

class MealListActivity : AppCompatActivity(), MealListAdapter.Callback, GoogleApiClient.ConnectionCallbacks {
  private var adapter: MealListAdapter? = null
  private lateinit var client: GoogleApiClient
  private var connectedNode: List<Node>? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    val meals = MealStore.fetchMeals(this)
    adapter = MealListAdapter(meals, this)
    list.adapter = adapter
    list.layoutManager = LinearLayoutManager(this)

    client = GoogleApiClient.Builder(this).addApi(Wearable.API).addConnectionCallbacks(this).build()
    client.connect()
  }

  override fun mealClicked(meal: Meal) {
    val gson = Gson()
    connectedNode?.forEach{ node ->
      val bytes = gson.toJson(meal).toByteArray()
      Wearable.MessageApi.sendMessage(client, node.id, "/meal", bytes)

    }
  }

  override fun onConnected(bundle: Bundle?) {
    Wearable.NodeApi.getConnectedNodes(client).setResultCallback {
      connectedNode = it.nodes
      Wearable.DataApi.addListener(client){ data ->
        val meal = Gson().fromJson(String(data[0].dataItem.data), Meal::class.java)
        adapter?.updateMeal(meal)
    }
  }

  override fun onConnectionSuspended(code: Int) {
    connectedNode = null
  }
}
