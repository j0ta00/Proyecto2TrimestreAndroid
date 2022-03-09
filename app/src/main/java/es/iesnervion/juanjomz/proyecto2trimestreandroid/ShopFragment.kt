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
    private val categorias= listOf("Carne","Elect.","Mar","Hogar","Restaurar")
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

    override fun onResume() {
        super.onResume()
            viewLifecycleOwner.lifecycleScope.launch {
                var numeroProducto = vmTienda.cesta.value?.let {
                    vmTienda.bbdd.value?.CestaCrossRefEntity?.obtenerNumeroElementosEnLaCesta(
                        it.idCesta
                    )
                }
                if (numeroProducto == null) {
                    binding.counterFab.count = 0
                } else {
                    binding.counterFab.count = numeroProducto
                }
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
                        nombre="Filete de ternera",
                        descripcion = "Filete con certificación de carne kobe, proveniente de las mejores vacas del mundo",
                        precio=13.0,
                        foto = "https://www.freepnglogos.com/uploads/meat-png/meat-sally-18.png"
                        ,categoria=categorias[0]
                    ),
                    ProductEntity(
                        nombre="Carne roja",
                        descripcion ="Carne de la mejor calidad, precio asequible y gran sabor",
                        precio=4.0,
                        foto ="https://www.freepnglogos.com/uploads/meat-png/meat-png-transparent-image-pngpix-11.png",
                        categoria=categorias[0]),
                    ProductEntity(
                        nombre="Pollo Frito",
                        descripcion ="Mejor que el de kentucky!!, este pollo frito te dejará sin palabras",
                        precio=5.0,
                        foto ="https://www.freepnglogos.com/uploads/fried-chicken-png/home-texas-chicken-fried-chicken-oman-26.png",
                        categoria=categorias[0]),
                    ProductEntity(
                        nombre="Lavadora",
                        descripcion ="Electrodoméstico renovado, todas las piezas que fallaban fueron arregladas",
                        precio=40.0,
                        foto ="https://www.freepnglogos.com/uploads/washing-machine-png/washing-machine-washing-machines-front-load-washers-electrolux-0.png",
                        categoria=categorias[1]),
                    ProductEntity(
                        nombre="Ordenador",
                        descripcion ="El mejor ordenador del mercado, puede con todo!!",
                        precio=87.0,
                        foto ="https://www.freepnglogos.com/uploads/computer-png/download-computer-desktop-png-image-png-image-21.png",
                        categoria=categorias[1]),
                    ProductEntity(
                        nombre="Escoba",
                        descripcion ="Olvidate tú de limpiar ya que esta escoba es tan buena que casi limpiara ella sola por ti",
                        precio=10.0,
                        foto ="https://www.freepnglogos.com/uploads/broom-png/broom-halloween-graphics-3.png",
                        categoria=categorias[3]),
                    ProductEntity(
                        nombre="Dorada",
                        descripcion ="Recien pescada del mar cantábrico, un manjar de la mayor calidad",
                        precio=7.0,
                        foto ="https://www.freepnglogos.com/uploads/fish-png/fish-png-systomus-sarana-wikispecies-13.png",
                        categoria=categorias[2]),
                    ProductEntity(
                        nombre="Sardina",
                        descripcion ="Buenisimas a la plancha y con un chorreón de jugo de limón",
                        precio=5.0,
                        foto ="https://www.freepnglogos.com/uploads/fish-png/fish-png-high-resolution-web-icons-png-36.png",
                        categoria=categorias[2]),
                    ProductEntity(
                        nombre="Cangrejo",
                        descripcion ="Perfecto para una cena de navidad en familia",
                        precio=9.0,
                        foto ="https://www.freepnglogos.com/uploads/crab-png/live-dungeness-crab-dolly-fish-market-15.png",
                        categoria=categorias[2])

                )
                )
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
        }
        if(vmTienda.cesta.value!=null) {
            viewLifecycleOwner.lifecycleScope.launch {
                var numeroProducto = vmTienda.cesta.value?.let {
                    vmTienda.bbdd.value?.CestaCrossRefEntity?.obtenerNumeroElementosEnLaCesta(
                        it.idCesta
                    )
                }

                if (numeroProducto == null) {
                    binding.counterFab.count = 0
                } else {
                    binding.counterFab.count = numeroProducto
                }
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
        if(p2!=4) {
            listaProductosFiltrada =
                ArrayList(ArrayList(vmTienda.listaProductos!!.value).filter { producto -> producto.categoria == categorias[p2] })

        }else{
            listaProductosFiltrada =ArrayList(ArrayList(vmTienda.listaProductos!!.value))
        }
        binding.rcvProductos.adapter =
            AdaptadorProducto(listaProductosFiltrada) { onItemSelected(it) }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        listaProductosFiltrada=ArrayList(vmTienda.listaProductos!!.value)
        binding.rcvProductos.adapter=AdaptadorProducto(listaProductosFiltrada) { onItemSelected(it) }
    }


}