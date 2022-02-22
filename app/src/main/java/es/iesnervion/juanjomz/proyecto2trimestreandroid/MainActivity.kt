package es.iesnervion.juanjomz.proyecto2trimestreandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.room.Room
import es.iesnervion.juanjomz.proyecto2trimestreandroid.databinding.ActivityMainBinding
import room.AppDatabase

class MainActivity : AppCompatActivity() {
    val modelTienda: ViewModelTienda by viewModels()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        modelTienda.bbdd.postValue(Room.databaseBuilder(
            binding.root.context, AppDatabase::class.java, AppDatabase.DATABASE_NAME
        ).allowMainThreadQueries().build())
    }
}
