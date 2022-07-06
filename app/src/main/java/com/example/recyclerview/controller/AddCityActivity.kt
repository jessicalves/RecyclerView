package com.example.recyclerview.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.recyclerview.R
import com.example.recyclerview.model.City
import com.example.recyclerview.model.DataStore

class AddCityActivity : AppCompatActivity() {

    var position = -1
    var city = City("", 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_city)

        val txtCity = findViewById<EditText>(R.id.txtCity)
        val txtPeople = findViewById<EditText>(R.id.txtPeople)

        val info = intent.getIntExtra("info", 0)
        if (info == 2) {
            position = intent.getIntExtra("position", 0)
            city = DataStore.getCity(position)

            txtCity.setText(city.name)
            txtPeople.setText(city.people.toString())
        }

        val btnSalve = findViewById<Button>(R.id.btnSave)
        val btnCancel = findViewById<Button>(R.id.btnCancel)

        btnSalve.setOnClickListener {
            city.name = txtCity.text.toString()
            city.people = txtPeople.text.toString().toInt()

            if(position == -1){
                DataStore.addCity(city)
            }
            else{
                DataStore.editCity(city,position)
            }
            val intent = Intent().apply {
                putExtra("city", city.name)
            }
            setResult(RESULT_OK, intent)
            finish()
        }

        btnCancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}