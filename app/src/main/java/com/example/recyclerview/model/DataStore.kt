package com.example.recyclerview.model
import android.content.Context

object DataStore {
    var cities : MutableList<City> = arrayListOf()
        private set

    private var myContext : Context? = null

    fun setContext(value:Context){
        myContext = value
    }

    init {
        addCity(City("Curitiba",18000000))
        addCity(City("Pinhalão",40000000))
        addCity(City("Londrina",575377))
    }

    fun getCity(position : Int) : City{
        return cities.get(position)
    }

    fun addCity(city: City){
        cities.add(city)
    }

    fun editCity(city: City, position:Int){
        cities.set(position, city)
    }

    fun removeCity(position: Int){
        cities.removeAt(position)
    }
}