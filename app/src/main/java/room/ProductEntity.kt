package room

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id : Long=0,
    @ColumnInfo(name = "nombre")
    @NonNull
    var nombre : String,
    @ColumnInfo(name = "descripcion")
    @NonNull
    var descripcion : String,
    @ColumnInfo(name = "precio")
    @NonNull
    var precio : Double,
    @ColumnInfo(name = "foto")
    var foto : String,
    @ColumnInfo(name = "categoria")
    var categoria: String
)
