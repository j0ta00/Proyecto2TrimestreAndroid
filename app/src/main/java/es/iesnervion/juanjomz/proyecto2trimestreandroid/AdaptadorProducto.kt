package es.iesnervion.juanjomz.proyecto2trimestreandroid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import es.iesnervion.juanjomz.proyecto2trimestreandroid.databinding.RowlayoutBinding
import room.ProductEntity

class AdaptadorProducto(val productEntities: List<ProductEntity>, private val onClickListener:(ProductEntity)->Unit):
    RecyclerView.Adapter<AdaptadorProducto.MyViewHolder>(){
    class MyViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val binding= RowlayoutBinding.bind(view)
        fun render(productEntity: ProductEntity, onClickListener:(ProductEntity)->Unit){
            binding.nombreProducto.text=productEntity.nombre
            binding.precioProducto.text="Precio: "+productEntity.precio.toString()+" €"
            Glide.with(view.context).load(productEntity.foto).into(binding.fotoProducto)
            itemView.setOnClickListener{
                onClickListener(productEntity)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        return MyViewHolder(layoutInflater.inflate(R.layout.rowlayout,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.render(productEntities[position],onClickListener)
    }

    override fun getItemCount(): Int = productEntities.size
}