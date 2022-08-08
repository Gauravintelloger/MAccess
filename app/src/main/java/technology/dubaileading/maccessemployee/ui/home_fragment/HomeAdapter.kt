package technology.dubaileading.maccessemployee.ui.home_fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.rest.entity.NotificationData
import technology.dubaileading.maccessemployee.rest.entity.PostData
import technology.dubaileading.maccessemployee.rest.entity.Posts

class HomeAdapter( private val context: Context) : RecyclerView.Adapter<HomeAdapter.PostsViewHolder>() {

    private var dataList = ArrayList<PostData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_feeds, parent, false)

        return PostsViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {

        if(position == 1){
            holder.view.visibility = View.VISIBLE
        }
        else holder.view.visibility = View.GONE
        var url = dataList[position].imageUrl
        Glide.with(context).load(url).into(holder.postImage)
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
        //var status_img = itemView.findViewById<View>(R.id.status_img) as ImageView
    }

}