package technology.dubaileading.maccessuser.ui.home_fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import technology.dubaileading.maccessuser.R

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.VH>() {

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var view = itemView.findViewById<View>(R.id.gap)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_feeds, parent, false)

        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
//        holder.view.visibility = View.VISIBLE
        if(position == 1){
            holder.view.visibility = View.VISIBLE
        }
        else holder.view.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return 2;
    }

}