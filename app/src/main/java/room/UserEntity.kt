package room

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "users")
data class UserEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id : Long=0,
    @ColumnInfo(name = "user")
    @NonNull
    var user : String,
    @ColumnInfo(name = "password")
    @NonNull
    var password : String
)
