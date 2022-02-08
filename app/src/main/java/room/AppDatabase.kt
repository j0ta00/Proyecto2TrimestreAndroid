package room

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities=[UserEntity::class],version=1)
abstract class AppDatabase:RoomDatabase() {
    abstract val userDao:UserDao

    companion object{
        const val DATABASE_NAME="db-Shop"

    }
}