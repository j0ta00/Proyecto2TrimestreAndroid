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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.room.Room
import es.iesnervion.juanjomz.proyecto2trimestreandroid.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch
import room.AppDatabase
import room.UserDao
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
    val vmTienda: ViewModelTienda by activityViewModels()
    private lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.img.setClipToOutline(true)
        binding = FragmentLoginBinding.bind(view)
        binding.logBtn.setOnClickListener(this)
        binding.regBtn.setOnClickListener(this)
    }

    override fun onClick(btn: View?) {
        var users: List<UserEntity>?=null
        if (btn != null) {
            if (btn != binding.regBtn) {
                val user = UserEntity(
                    dni = "prueba",
                    password = "prueba",
                    email = "",
                    name = "",
                    surname = "",
                    phone = ""
                )
                viewLifecycleOwner.lifecycleScope.launch {
                    vmTienda.bbdd.value?.userDao?.insert(user)
                    users = vmTienda.bbdd.value?.userDao?.getAllUsers(
                        binding.edDni.text.toString(),
                        binding.edPassword.text.toString()
                    )
                }
                if (users != null && users!!.isNotEmpty()) {
                    Toast.makeText(context, "registrado con éxito", Toast.LENGTH_SHORT).show()
                } else {
                    if (binding.edDni.text.isNullOrEmpty()) {
                        binding.edDni.error = getString(R.string.emptyField)
                    }
                    if (binding.edPassword.text.isNullOrEmpty()) {
                        binding.edPassword.error = getString(R.string.emptyField)
                    }
                    if (!binding.edPassword.text.isNullOrEmpty() && !binding.edDni.text.isNullOrEmpty()) {
                        Toast.makeText(context, "datos incorrectos", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Navigation.findNavController(binding.root).navigate(R.id.registerFragment);
            }
        }
    }


}