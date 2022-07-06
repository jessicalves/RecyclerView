package com.example.recyclerview.controller

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.Menu
import android.view.MotionEvent
import android.widget.Toolbar
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerview.R
import com.example.recyclerview.model.DataStore
import com.example.recyclerview.view.CityAdapter
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private var adapter: CityAdapter? = null
    private var rcvCities: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val layMain = findViewById<CoordinatorLayout>(R.id.layMain)
        val fab = findViewById<FloatingActionButton>(R.id.fab)

        rcvCities = findViewById<RecyclerView>(R.id.rcvCities)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rcvCities?.layoutManager = layoutManager
        adapter = CityAdapter(DataStore.cities)
        rcvCities?.adapter = adapter

        updateCollapsingToolbar()
        var resultEditLaunch =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                //Informacao aceita
                if (it.resultCode == RESULT_OK) {
                    adapter?.notifyDataSetChanged()
                    Snackbar.make(
                        layMain,
                        "Cidade ${it.data?.getStringExtra("city")} atualizada com sucesso.",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }

        var resultAddLaunch =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                //Informacao aceita
                if (it.resultCode == RESULT_OK) {
                    adapter?.notifyDataSetChanged()
                    Snackbar.make(
                        layMain,
                        "Cidade ${it.data?.getStringExtra("city")} adicionada com sucesso.",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }

        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, AddCityActivity::class.java).apply {
                this.putExtra("info", 1)
            }
            resultAddLaunch.launch(intent)
        }

        val gestureDetector =
            GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                    e?.let {
                        val view = rcvCities?.findChildViewUnder(it.x, it.y)

                        view?.let {
                            val position = rcvCities?.getChildAdapterPosition(it)
                            position?.let {
                                val city = DataStore.getCity(it)

                                Snackbar.make(layMain, city.name, Snackbar.LENGTH_SHORT).show()

                                //Desmonstra a intencao de abrir a tela
                                val intent =
                                    Intent(this@MainActivity, AddCityActivity::class.java).apply {
                                        this.putExtra("info", 2)
                                        this.putExtra("position", position)
                                    }
                                resultEditLaunch.launch(intent)
//                            startActivity(intent)
                            }
                        }
                    }
                    return super.onSingleTapConfirmed(e)
                }

                override fun onLongPress(e: MotionEvent?) {
                    e?.let {
                        val view = rcvCities?.findChildViewUnder(it.x, it.y)
                        view?.let {
                            val position = rcvCities?.getChildAdapterPosition(it)
                            position?.let {
                                val city = DataStore.getCity(it)
                                val dialog = AlertDialog.Builder(this@MainActivity)

                                dialog.setTitle("App de Cidades")
                                dialog.setMessage("Tem certeza que deseja remover esta cidade?")
                                dialog.setPositiveButton(
                                    "Excluir",
                                    DialogInterface.OnClickListener { dialogInterface, i ->
                                        DataStore.removeCity(position)
                                        adapter?.notifyDataSetChanged()
                                        updateCollapsingToolbar()
                                        val layMain = findViewById<CoordinatorLayout>(R.id.layMain)
                                        Snackbar.make(
                                            layMain,
                                            "Cidade ${city.name} excluída com sucesso.",
                                            Snackbar.LENGTH_LONG
                                        ).show()
                                    })
                                dialog.setNegativeButton("Cancelar", null)
                                dialog.show()
                            }
                        }
                    }
                    super.onLongPress(e)
                }
            })

        rcvCities?.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                TODO("Not yet implemented")
            }

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                val child = rv.findChildViewUnder(e.x, e.y)
                return (child != null && gestureDetector.onTouchEvent(e))
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onMenuOpened(featureId: Int, menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    fun updateCollapsingToolbar() {
        val collepsingToolbar = findViewById<CollapsingToolbarLayout>(R.id.collapsedToolbar)

        collepsingToolbar.title = "Cidades: ${DataStore.cities.size}"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


    }
}