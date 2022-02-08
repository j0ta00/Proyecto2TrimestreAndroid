package room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
@Dao
interface UserDao {
    @Insert
    fun insert(user: UserEntity)
    @Query("SELECT*FROM users Where user=:user AND password=:password")
    fun getAllUsers(user:String,password:String): List<UserEntity>
}