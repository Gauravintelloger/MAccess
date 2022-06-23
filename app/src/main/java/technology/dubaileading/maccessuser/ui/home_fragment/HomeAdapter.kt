package technology.dubaileading.maccessuser.ui.home_fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import technology.dubaileading.maccessuser.R

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.VH>() {

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_feeds, parent, false)

        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

    }

    override fun getItemCount(): Int {
        return 2;
    }

}