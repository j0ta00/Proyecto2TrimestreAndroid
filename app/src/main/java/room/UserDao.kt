package room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: UserEntity)
    @Query("SELECT*FROM users Where dni=:dni AND password=:password")
    suspend fun getAllUsers(dni:String,password:String): List<UserEntity>
}