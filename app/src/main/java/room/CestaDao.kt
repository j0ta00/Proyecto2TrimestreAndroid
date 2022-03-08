package room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CestaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertarCesta(cesta : CestaEntity)

    @Query("SELECT * FROM cesta WHERE idUsuario=:idUsuario AND estado=0")
    fun obtenerCestaSinProcesar(idUsuario: Long): CestaEntity
}