package es.iesnervion.juanjomz.proyecto2trimestreandroid

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.room.Room
import es.iesnervion.juanjomz.proyecto2trimestreandroid.databinding.FragmentLoginBinding
import room.AppDatabase
import room.UserEntity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment(), View.OnClickListener {
private lateinit var database:AppDatabase
private lateinit var binding:FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentLoginBinding.inflate(inflater, container, false)
        database= Room.databaseBuilder(
            binding.root.context,AppDatabase::class.java,AppDatabase.DATABASE_NAME
        ).allowMainThreadQueries().build()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=FragmentLoginBinding.bind(view)
        binding.logBtn.setOnClickListener(this)
    }

    override fun onClick(btn: View?) {
        val user=UserEntity(user="prueba",password ="prueba")
        database.userDao.insert(user)
        val users=database.userDao.getAllUsers(binding.edUser.text.toString(),binding.edPassword.text.toString())
        if(users.isNotEmpty()){
            Toast.makeText(context,"registrado con éxito", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context,"datos incorrectos", Toast.LENGTH_SHORT).show()
        }
    }


}