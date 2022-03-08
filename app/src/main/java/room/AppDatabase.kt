package room

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities=[UserEntity::class,ProductEntity::class,CestaEntity::class,CestaCrossRefEntity::class],version=1)
abstract class AppDatabase:RoomDatabase() {
    abstract val userDao:UserDao
    abstract val productDao:ProductDao
    abstract val CestaDao:CestaDao
    abstract val CestaCrossRefEntity:CestaCrossRefDao
    companion object{
        const val DATABASE_NAME="db-Shop"

    }
}