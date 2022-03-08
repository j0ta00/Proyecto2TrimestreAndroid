package room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
@Dao
interface UserDao {
    @Insert
     fun insertarUsuario(user: UserEntity)
    @Query("SELECT*FROM users Where dni=:dni AND password=:password")
    fun obtenerTodosLosUsuarios(dni:String, password:String): List<UserEntity>
    @Query("SELECT*FROM users Where dni=:dni AND password=:password")
    fun obtenerUsuario(dni:String, password:String): UserEntity
}