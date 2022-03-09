package room

import androidx.room.*

@Dao
interface CestaCrossRefDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertarCestaCrossRef(shoppingCartProductCrossRef: CestaCrossRefEntity)

    @Query("SELECT COUNT(*) FROM cesta_cross_ref WHERE idCesta=:idCesta")
    fun obtenerNumeroElementosEnLaCesta(idCesta: Long): Int

    @Query("SELECT * FROM cesta_cross_ref WHERE idCesta=:idCesta")
    fun obtenerElementosEnLaCesta(idCesta: Long): List<CestaCrossRefEntity>

    @Delete
    fun borrarProductoDeCesta (lineaCesta:CestaCrossRefEntity)
}