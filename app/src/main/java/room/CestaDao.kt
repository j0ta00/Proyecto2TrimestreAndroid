package room

import androidx.room.*

@Dao
interface CestaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertarCesta(cesta : CestaEntity)

    @Query("SELECT * FROM cesta WHERE idUsuario=:idUsuario AND estado=0")
    fun obtenerCestaSinProcesar(idUsuario: Long): CestaEntity
    @Update
    fun actualizarEstadoCesta(cesta:CestaEntity)
}