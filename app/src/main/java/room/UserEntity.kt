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
    @ColumnInfo(name = "dni")
    @NonNull
    var dni : String,
    @ColumnInfo(name = "password")
    @NonNull
    var password : String,
    @ColumnInfo(name = "email")
    @NonNull
    var email : String,
    @ColumnInfo(name = "phone")
    var phone : String,
    @ColumnInfo(name = "name")
    @NonNull
    var name : String,
    @ColumnInfo(name = "surname")
    var surname : String

)
