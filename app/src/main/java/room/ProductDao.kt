package room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
@Dao
interface ProductDao {

    @Query("SELECT*FROM products")
    fun obtenerTodosLosProductos():List<ProductEntity>
    @Insert
    fun insertarProductos(products: List<ProductEntity>)
}