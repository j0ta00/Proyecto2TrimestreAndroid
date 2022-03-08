package es.iesnervion.juanjomz.proyecto2trimestreandroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import es.iesnervion.juanjomz.proyecto2trimestreandroid.databinding.FragmentCestaBinding
import es.iesnervion.juanjomz.proyecto2trimestreandroid.databinding.FragmentLoginBinding
import es.iesnervion.juanjomz.proyecto2trimestreandroid.databinding.FragmentShopBinding
import kotlinx.coroutines.launch
import room.CestaCrossRefEntity
import room.ProductEntity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CestaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CestaFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentCestaBinding
    private var cestaCross: List<CestaCrossRefEntity>?=null
    val vmTienda: ViewModelTienda by activityViewModels()
    private lateinit var cesta:List<ProductEntity>
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
        binding = FragmentCestaBinding.inflate(inflater, container, false)

        viewLifecycleOwner.lifecycleScope.launch{
            cestaCross=vmTienda.cesta.value?.idCesta?.let {
                vmTienda.bbdd.value?.CestaCrossRefEntity?.obtenerElementosEnLaCesta(
                    it
                )
            }
            if(cestaCross!=null) {
               cesta=ArrayList(vmTienda.listaProductos.value).filter { producto ->
                    cestaCross!!.contains(
                        CestaCrossRefEntity(
                            vmTienda.cesta.value!!.idCesta,
                            producto.id
                        )
                    )
                }
            }
        }
        binding.RecyclerViewCesta.layoutManager=LinearLayoutManager(this.requireContext())
        binding.RecyclerViewCesta.adapter=AdaptadorProducto(cesta){onItemSelected(it)}
        return binding.root
    }
    fun onItemSelected(productEntity:ProductEntity){}
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CestaFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CestaFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}