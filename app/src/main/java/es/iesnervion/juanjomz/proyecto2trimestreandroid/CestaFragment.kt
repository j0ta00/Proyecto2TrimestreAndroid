package es.iesnervion.juanjomz.proyecto2trimestreandroid

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import es.iesnervion.juanjomz.proyecto2trimestreandroid.databinding.*
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
class CestaFragment : Fragment(),View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentCestaBinding
    private var cestaCross: List<CestaCrossRefEntity>?=null
    private var total:Double=0.0
    val vmTienda: ViewModelTienda by activityViewModels()
    private var cesta:List<ProductEntity>?=null
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
        binding.RecyclerViewCesta.adapter= cesta?.let { AdaptadorCesta(it){onItemSelected(it)} }
        calcularElTotal()
        return binding.root
    }

    fun calcularElTotal(){
        total=0.0
        cesta?.forEach{
            producto->if(producto!=null){
                total+=producto.precio
            }
        }
        if(total==null){
            total=0.0
        }
        binding.Total.text="Total: "+total+" €"
    }
    fun onItemSelected(productEntity:ProductEntity){
        viewLifecycleOwner.lifecycleScope.launch{
            cestaCross?.find { cesta->cesta.idProducto==productEntity.id}?.let {
                vmTienda.bbdd.value?.CestaCrossRefEntity?.borrarProductoDeCesta(
                    it
                )
            }
            cesta= cesta?.filter { producto->producto.id!=productEntity.id }
            binding.RecyclerViewCesta.adapter= cesta?.let { AdaptadorCesta(it){onItemSelected(it)} }
        }
        calcularElTotal()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.btnProcesarCesta).setOnClickListener(this)
    }
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

    class AdaptadorCesta(val productEntities: List<ProductEntity>, private val onClickListener:(ProductEntity)->Unit):
        RecyclerView.Adapter<AdaptadorCesta.MyViewHolder>(){
        class MyViewHolder(val view: View): RecyclerView.ViewHolder(view){
            val binding= RowLayout2Binding.bind(view)
            fun render(productEntity: ProductEntity, onClickListener:(ProductEntity)->Unit){
                binding.nombreProducto.text=productEntity.nombre
                binding.precioProducto.text="Precio: "+productEntity.precio.toString()+" €"
                Glide.with(view.context).load(productEntity.foto).into(binding.fotoProducto)
                binding.btnEliminarProductoDeCesta.setOnClickListener{
                    onClickListener(productEntity)

                }
            }
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val layoutInflater= LayoutInflater.from(parent.context)
            return MyViewHolder(layoutInflater.inflate(R.layout.row_layout_2,parent,false))
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.render(productEntities[position],onClickListener)
        }

        override fun getItemCount(): Int = productEntities.size
    }

    override fun onClick(p0: View?) {
        if(cesta!=null){
            viewLifecycleOwner.lifecycleScope.launch{
                vmTienda.cesta.value?.estado=1
                vmTienda.bbdd?.value?.CestaDao?.actualizarEstadoCesta((vmTienda.cesta.value!!))
                cesta=null
                vmTienda.cesta.value=null
                binding.RecyclerViewCesta.adapter= cesta?.let { AdaptadorCesta(it){onItemSelected(it)} }
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/html"
                intent.putExtra(Intent.EXTRA_EMAIL, vmTienda.usuario.value?.email)
                intent.putExtra(Intent.EXTRA_SUBJECT, "Compra")
                intent.putExtra(Intent.EXTRA_TEXT, "La compra se ha realizado, llegará en 2hrs y costará un total de "+total)
                startActivity(Intent.createChooser(intent, "Send Email"))
                calcularElTotal()
                Toast.makeText(context, "Compra confirmada, revise su correo electrónnico", Toast.LENGTH_SHORT).show()
            }
        }
    }
}