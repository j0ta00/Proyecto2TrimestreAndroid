package room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
@Dao
interface ProductDao {

    @Query("SELECT*FROM products")
    fun obtenerTodosLosProductos():List<ProductEntity>
    @Query("SELECT Id FROM products Where nombre=:nombre")
    fun obtenerIdProducto(nombre:String):Long
    @Insert
    fun insertarProductos(products: List<ProductEntity>)
}