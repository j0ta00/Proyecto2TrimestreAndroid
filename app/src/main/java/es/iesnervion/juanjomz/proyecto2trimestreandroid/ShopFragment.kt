package es.iesnervion.juanjomz.proyecto2trimestreandroid

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andremion.counterfab.CounterFab
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import es.iesnervion.juanjomz.proyecto2trimestreandroid.databinding.FragmentShopBinding
import es.iesnervion.juanjomz.proyecto2trimestreandroid.databinding.RowlayoutBinding
import kotlinx.coroutines.launch
import room.CestaCrossRefEntity
import room.CestaEntity
import room.ProductEntity
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShopFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShopFragment : Fragment(), View.OnClickListener, AdapterView.OnItemSelectedListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val categorias= listOf("Carne","Electronica","Pescado","Limpieza")
    val vmTienda: ViewModelTienda by activityViewModels()
    lateinit var listaProductosFiltrada: ArrayList<ProductEntity>
    private lateinit var binding: FragmentShopBinding
    private lateinit var productoSeleccionado:ProductEntity
    private lateinit var cestaCrossRef:List<CestaCrossRefEntity>
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
        binding= FragmentShopBinding.inflate(inflater, container, false)
        binding.rcvProductos.layoutManager=GridLayoutManager(context,2)

        viewLifecycleOwner.lifecycleScope.launch {
            vmTienda.listaProductos.value=
                vmTienda.bbdd.value?.productDao?.obtenerTodosLosProductos(
                )
        }
        if(vmTienda.listaProductos.value==null || vmTienda.listaProductos.value!!.isEmpty()) {
            viewLifecycleOwner.lifecycleScope.launch {
                vmTienda.bbdd.value?.productDao?.insertarProductos(listOf(
                    ProductEntity(
                        nombre="Salchichon",
                        descripcion = "Sabroso salchichón de extremadura, de una alta calidad, para un paladar gourmet",
                        precio=3.0,
                        foto = "https://www.supermercadosmas.com/media/catalog/product/cache/d91bc430dbe2e3d899436802c7aa5233/i/m/import_catalog_images_29_21_292188_v7.jpg"
                        ,categoria=categorias[0]
                    ),
                    ProductEntity(
                        nombre="Jamón",
                        descripcion ="Jamón de jabugo, manjar de origen español, proveniente del cerdo ibérico de más alta calidad",
                        precio=12.0,
                        foto ="https://i0.wp.com/commememucho.com/wp-content/uploads/2020/07/IMG_20190618_131750902.jpg?fit=750%2C563&ssl=1",
                        categoria=categorias[0])
                ))
                vmTienda.listaProductos.value=
                    vmTienda.bbdd.value?.productDao?.obtenerTodosLosProductos(
                    )
            }
        }
        if(vmTienda.cesta.value==null) {
            viewLifecycleOwner.lifecycleScope.launch {
                vmTienda.cesta.value = vmTienda.usuario.value?.let {
                    vmTienda.bbdd.value?.CestaDao?.obtenerCestaSinProcesar(
                        it.id
                    )
                }
            }
                if(vmTienda.cesta.value!=null) {
                    binding.counterFab.count=vmTienda.bbdd.value?.CestaCrossRefEntity?.obtenerNumeroElementosEnLaCesta(vmTienda.cesta.value!!.idCesta)!!
                }
        }

            listaProductosFiltrada=ArrayList(vmTienda.listaProductos.value)
            binding.rcvProductos.adapter = AdaptadorProducto(listaProductosFiltrada) { onItemSelected(it) }
        setHasOptionsMenu(true)
        return binding.root
    }
    fun onItemSelected(productEntity:ProductEntity){
        var detalles_layout = layoutInflater.inflate(
        R.layout.detalles_productos_layout, null)
        productoSeleccionado=productEntity
        Glide.with(detalles_layout.context).load(productEntity.foto).into(detalles_layout.findViewById<ImageView>(R.id.DetallesFotoProducto))
        detalles_layout.findViewById<TextView>(R.id.DetallesNombreProducto).text=productEntity.nombre
        detalles_layout.findViewById<TextView>(R.id.DetallesDescripcionProducto).text=productEntity.descripcion
        detalles_layout.findViewById<TextView>(R.id.DetallesCategoriaProducto).text="Categoría: "+productEntity.categoria
        detalles_layout.findViewById<TextView>(R.id.DetallesPrecioProducto).text="Precio: "+productEntity.precio.toString()+" €"
        detalles_layout.findViewById<Button>(R.id.btnAnhiadirProductoALaCesta).setOnClickListener(this)
        MaterialAlertDialogBuilder(this.requireContext())
            .setTitle("Detalles del producto")
            .setView(detalles_layout)
            .show()
    }

    @Override
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_item,menu)
        super.onCreateOptionsMenu(menu, inflater)
        val botonFiltros=menu?.findItem(R.id.Filtros)?.actionView as ImageButton
        botonFiltros.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_filter_alt_24,null))
        botonFiltros.setOnClickListener(this)
        val searchView=menu?.findItem(R.id.searchViewProductos)?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                listaProductosFiltrada.clear()
                val searchText= p0!!.lowercase(Locale.getDefault())
                if(searchText.isNotEmpty() && searchText.isNotBlank()){
                    vmTienda.listaProductos.value!!.forEach() {
                        if(it.nombre.lowercase(Locale.getDefault()).contains(searchText))
                        {
                            listaProductosFiltrada.add(it)
                        }
                    }
                    binding.rcvProductos.adapter?.notifyDataSetChanged()
                }else{
                    listaProductosFiltrada.clear()
                    listaProductosFiltrada.addAll(ArrayList(vmTienda.listaProductos.value))
                    binding.rcvProductos.adapter?.notifyDataSetChanged()
                }
                return false
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<CounterFab>(R.id.counter_fab).setOnClickListener(this)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ShopFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ShopFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }



    override fun onClick(p0: View?) {
        if (p0!!.id != R.id.btnAnhiadirProductoALaCesta && p0!!.id != R.id.counter_fab) {
            var filtrado_layout = layoutInflater.inflate(
                R.layout.filtrado, null
            );
            val opcionesOrdenacion = arrayOf("Ordenar por precio", "Ordenar por nombre")
            val spinnerCategorias = filtrado_layout.findViewById<Spinner>(R.id.spinnerFiltrado)
            var adaptador = ArrayAdapter(
                this.requireContext(),
                android.R.layout.simple_spinner_item,
                categorias
            )
            adaptador.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            spinnerCategorias.adapter = adaptador
            spinnerCategorias.setOnItemSelectedListener(this)
            MaterialAlertDialogBuilder(this.requireContext())
                .setTitle("")
                .setNeutralButton("Cancelar") { dialog, which ->
                }
                .setPositiveButton(resources.getString(R.string.ok)) { dialog, which ->
                }
                .setSingleChoiceItems(opcionesOrdenacion, -1) { dialog, which ->
                    if (which == 0) {
                        listaProductosFiltrada =
                            ArrayList(listaProductosFiltrada.sortedByDescending { it.precio })
                        binding.rcvProductos.adapter =
                            AdaptadorProducto(listaProductosFiltrada) { onItemSelected(it) }
                    } else {
                        listaProductosFiltrada =
                            ArrayList(listaProductosFiltrada.sortedBy { it.nombre })
                        binding.rcvProductos.adapter =
                            AdaptadorProducto(listaProductosFiltrada) { onItemSelected(it) }
                    }
                    dialog.dismiss()
                }.setView(filtrado_layout)
                .show()
        }else if(p0!!.id == R.id.counter_fab){
            Navigation.findNavController(binding.root).navigate(R.id.cestaFragment)
        }else{
            anhiadirProductoACesta()
        }
    }
    private fun anhiadirProductoACesta(){
        if(vmTienda.cesta.value==null){
            viewLifecycleOwner.lifecycleScope.launch {
                var cestaEntity=CestaEntity(idCesta = 0,estado=0,idUsuario=vmTienda.usuario.value!!.id)
                vmTienda.bbdd.value?.CestaDao?.insertarCesta(cestaEntity)
                vmTienda.cesta.value=vmTienda.bbdd.value?.CestaDao?.obtenerCestaSinProcesar(vmTienda.usuario.value!!.id)!!
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            vmTienda.bbdd.value?.CestaCrossRefEntity?.insertarCestaCrossRef(CestaCrossRefEntity(vmTienda.cesta.value!!.idCesta,productoSeleccionado.id))
            binding.counterFab.count=vmTienda.bbdd.value?.CestaCrossRefEntity?.obtenerNumeroElementosEnLaCesta(vmTienda.cesta.value!!.idCesta)!!
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        listaProductosFiltrada=ArrayList(ArrayList(vmTienda.listaProductos!!.value).filter{producto->producto.categoria==categorias[p2]})
        binding.rcvProductos.adapter=AdaptadorProducto(listaProductosFiltrada) { onItemSelected(it) }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        listaProductosFiltrada=ArrayList(vmTienda.listaProductos!!.value)
        binding.rcvProductos.adapter=AdaptadorProducto(listaProductosFiltrada) { onItemSelected(it) }
    }


}