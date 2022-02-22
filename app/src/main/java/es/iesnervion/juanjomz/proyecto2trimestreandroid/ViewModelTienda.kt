package es.iesnervion.juanjomz.proyecto2trimestreandroid

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import room.AppDatabase

class ViewModelTienda : ViewModel() {
    val bbdd= MutableLiveData<AppDatabase>()
}