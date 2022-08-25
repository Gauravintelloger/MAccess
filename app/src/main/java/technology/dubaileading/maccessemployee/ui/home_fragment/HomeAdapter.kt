package technology.dubaileading.maccessemployee.ui.home_fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.rest.entity.PostData

class HomeAdapter( private val context: Context, private val onLikeClickListener: onLikeClickListener) : RecyclerView.Adapter<HomeAdapter.PostsViewHolder>() {

    private var dataList = ArrayList<PostData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_feeds, parent, false)

        return PostsViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        var url = dataList[position].imageUrl
        holder.postImage.load(url){
            transformations(RoundedCornersTransformation(30f))
        }
        holder.crated_at.text = dataList[position].readableCreatedAt
        holder.title.text = dataList[position].caption
        holder.likeCount.text = dataList[position].likedUsersCount.toString()

        holder.like.setOnClickListener {
            onLikeClickListener.onLikeClick(dataList[position], position)
        }
        
        if (dataList[position].liked!!){
            holder.like_img.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.like_red))
        }else{
            holder.like_img.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.like))
        }
    }


    override fun getItemCount(): Int {
        return dataList.size;
    }

    fun addList(postData: ArrayList<PostData>){
        dataList.clear()
        dataList.addAll(postData)
        notifyDataSetChanged()
    }



    class PostsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var view = itemView.findViewById<View>(R.id.gap)
        var postImage = itemView.findViewById<View>(R.id.postImage) as ImageView
        var like_img = itemView.findViewById<View>(R.id.like_img) as ImageView
        var crated_at = itemView.findViewById<View>(R.id.crated_at) as TextView
        var likeCount = itemView.findViewById<View>(R.id.likeCount) as TextView
        var title = itemView.findViewById<View>(R.id.title) as TextView
        var like = itemView.findViewById<View>(R.id.like) as LinearLayout
    }

}