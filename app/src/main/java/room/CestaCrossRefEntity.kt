package room

import androidx.room.Entity

@Entity(tableName = "cesta_cross_ref",
    primaryKeys = ["idCesta", "idProducto"])
data class CestaCrossRefEntity(
    val idCesta: Long,
    val idProducto: Long

)