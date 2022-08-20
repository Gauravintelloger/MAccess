package technology.dubaileading.maccessemployee.ui.requests

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import technology.dubaileading.maccessemployee.R

import technology.dubaileading.maccessemployee.rest.entity.OtherRequestsItem

class DocumentRequestAdapter(val context: Context) : RecyclerView.Adapter<DocumentRequestAdapter.DocumentRequestsViewHolder>() {

    private var otherRequestsData = ArrayList<OtherRequestsItem>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DocumentRequestsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.document_request_holder, parent, false)

        return DocumentRequestsViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: DocumentRequestsViewHolder,
        position: Int
    ) {
        holder.request_type.text = otherRequestsData[position].description
        holder.metadata.text = otherRequestsData[position].requesttype?.type

        holder.request_on.text = otherRequestsData[position].createdAt
        holder.status.text = otherRequestsData[position].statusId
        if (otherRequestsData[position].statusId.equals("pending")){
            holder.status.background = ContextCompat.getDrawable(context,R.drawable.pending_bg)
            holder.status.setTextColor(context.resources.getColor(R.color.text_color_yellow))
        } else if (otherRequestsData[position].statusId.equals("approved")){
            holder.status.background = ContextCompat.getDrawable(context,R.drawable.approved_bg)
            holder.status.setTextColor(context.resources.getColor(R.color.text_color_green))
        } else if (otherRequestsData[position].statusId.equals("declined")){
            holder.status.background = ContextCompat.getDrawable(context,R.drawable.declined_bg)
            holder.status.setTextColor(context.resources.getColor(R.color.text_color_red))
        }

    }

    override fun getItemCount(): Int {
        return otherRequestsData.size;
    }

    fun setData(otherRequestsItem: ArrayList<OtherRequestsItem>) {
        otherRequestsData?.addAll(otherRequestsItem)
        notifyDataSetChanged()
    }

    class DocumentRequestsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var request_type = itemView.findViewById<View>(R.id.request_type) as TextView
        var metadata = itemView.findViewById<View>(R.id.metadata) as TextView
        var request_on = itemView.findViewById<View>(R.id.request_on) as TextView
        var status = itemView.findViewById<View>(R.id.status) as TextView
    }
}
