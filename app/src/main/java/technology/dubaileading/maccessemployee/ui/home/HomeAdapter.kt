package technology.dubaileading.maccessemployee.ui.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.paging.LOGGER
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.databinding.RowPostBinding
import technology.dubaileading.maccessemployee.rest.entity.PostData

class HomeAdapter(
    var data: List<PostData?>,
    var onLikeClickListener: OnLikeClickListener
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding : RowPostBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.row_post,parent,false)
        context = parent.context

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data: PostData = data[position]!!
        holder.binding.data = data
//
//        Log.e("imagessss","https://636e-2401-4900-1c5a-f583-2d34-eec1-db18-c5f4.ngrok-free.app/storage/"+data.imageUrl)
//        holder.binding.postImage.load(data.imageUrl){
//           // transformations(RoundedCornersTransformation(30f))
//        }

        Glide.with(context).load(data.imageUrl)
            .transform(CenterInside(), RoundedCorners(24))
            .into(holder.binding.postImage)

        if (data.liked == true){
            holder.binding.likeImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.like_red))
        }else{
            holder.binding.likeImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.like))
        }

        holder.itemView.setOnClickListener {
            onLikeClickListener.onLikeCLicked(data, position)
        }
    }

    fun updateList(list: List<PostData?>) {
        this.data = list
        this.notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(var binding: RowPostBinding) :
        RecyclerView.ViewHolder(binding.root)

}

interface OnLikeClickListener {
    fun onLikeCLicked(bean: PostData, position: Int)
}