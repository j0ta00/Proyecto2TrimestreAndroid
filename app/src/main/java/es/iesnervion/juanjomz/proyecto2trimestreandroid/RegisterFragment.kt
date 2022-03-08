package es.iesnervion.juanjomz.proyecto2trimestreandroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.room.Room
import es.iesnervion.juanjomz.proyecto2trimestreandroid.databinding.FragmentLoginBinding
import es.iesnervion.juanjomz.proyecto2trimestreandroid.databinding.FragmentRegisterBinding
import kotlinx.coroutines.launch
import room.AppDatabase
import room.UserEntity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment :Fragment(), View.OnClickListener{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val vmTienda: ViewModelTienda by activityViewModels()
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentRegisterBinding.inflate(inflater, container, false)
        vmTienda.bbdd.value= Room.databaseBuilder(
            binding.root.context,AppDatabase::class.java,AppDatabase.DATABASE_NAME
        ).allowMainThreadQueries().build()
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegisterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.img.setClipToOutline(true)
        binding.regBtn.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        if(elementosEstanVacios() && binding.edPassword.text.toString() == binding.edPassword2.text.toString()) {
            val user = UserEntity(
                dni = binding.edDni.text.toString(),
                password = binding.edPassword.text.toString(),
                email = binding.edEmail.text.toString(),
                name = binding.edName.text.toString(),
                surname = binding.edSurname.text.toString(),
                phone = binding.edTelefono.text.toString()
            )
            viewLifecycleOwner.lifecycleScope.launch{
                vmTienda.bbdd.value?.userDao?.insertarUsuario(user)
            }
            Navigation.findNavController(binding.root).navigate(R.id.loginFragment);
        }else {
            if (!elementosEstanVacios() && binding.edPassword2.text!=binding.edPassword.text) {
                Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun elementosEstanVacios():Boolean{
        var estanRellenos=true
        if(binding.edDni.text.isNullOrEmpty()){
            binding.edDni.error=getString(R.string.emptyField)
            estanRellenos=false
        }
        if(binding.edEmail.text.isNullOrEmpty()){
            binding.edEmail.error=getString(R.string.emptyField)
            estanRellenos=false
        }
        if(binding.edName.text.isNullOrEmpty()){
            binding.edName.error=getString(R.string.emptyField)
            estanRellenos=false
        }
        if(binding.edPassword.text.isNullOrEmpty()){
            binding.edPassword.error=getString(R.string.emptyField)
            estanRellenos=false
        }
        if(binding.edPassword2.text.isNullOrEmpty()){
            binding.edPassword2.error=getString(R.string.emptyField)
            estanRellenos=false
        }

        return estanRellenos
    }
}