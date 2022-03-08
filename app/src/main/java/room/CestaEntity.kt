package room

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "cesta")
data class CestaEntity (
    @PrimaryKey(autoGenerate = true)
    val idCesta: Long,
    val estado: Long,
    val idUsuario: Long
        )