package technology.dubaileading.maccessemployee.ui.requests

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import technology.dubaileading.maccessemployee.R

class RequestsAdapter : RecyclerView.Adapter<RequestsAdapter.RequestsViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RequestsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.request_holder, parent, false)

        return RequestsViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: RequestsViewHolder,
        position: Int
    ) {

    }

    override fun getItemCount(): Int {
        return 8;
    }

    class RequestsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}
