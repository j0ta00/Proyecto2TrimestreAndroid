package es.iesnervion.juanjomz.proyecto2trimestreandroid

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import room.AppDatabase
import room.CestaEntity
import room.ProductEntity
import room.UserEntity

class ViewModelTienda : ViewModel() {
    val bbdd= MutableLiveData<AppDatabase>()
    var usuario=MutableLiveData<UserEntity>()
    var listaProductos=MutableLiveData<List<ProductEntity>>()
    var cestaProductos=MutableLiveData<List<ProductEntity>>()
    var cesta=MutableLiveData<CestaEntity>()
}