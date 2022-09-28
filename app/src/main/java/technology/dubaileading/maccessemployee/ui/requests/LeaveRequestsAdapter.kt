package technology.dubaileading.maccessemployee.ui.requests

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.rest.entity.LeaveRequestsItem


class LeaveRequestsAdapter(val context: Context,val onClickListener: leaveClickListener) : RecyclerView.Adapter<LeaveRequestsAdapter.RequestsViewHolder>() {

    private var leaveRequests = ArrayList<LeaveRequestsItem>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RequestsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.leave_request_holder, parent, false)

        return RequestsViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(
        holder: RequestsViewHolder,
        position: Int
    ) {
        holder.request_type.text = leaveRequests[position].description
        holder.metadata.text = leaveRequests[position].leaveType?.title
        holder.days.text = leaveRequests[position].days.toString()+" Days"
        holder.from_to.text = leaveRequests[position].fromDate +"  -  "+ leaveRequests[position].toDate
        holder.request_on.text = leaveRequests[position].createdAt
        holder.status.text = leaveRequests[position].status
        if (leaveRequests[position].status.equals("pending")){
            holder.status.background = ContextCompat.getDrawable(context,R.drawable.pending_bg)
            holder.status.setTextColor(context.resources.getColor(R.color.text_color_yellow))
        } else if (leaveRequests[position].status.equals("approved")){
            holder.status.background = ContextCompat.getDrawable(context,R.drawable.approved_bg)
            holder.status.setTextColor(context.resources.getColor(R.color.text_color_green))
        } else if (leaveRequests[position].status.equals("rejected")){
            holder.status.background = ContextCompat.getDrawable(context,R.drawable.declined_bg)
            holder.status.setTextColor(context.resources.getColor(R.color.text_color_red))
        }

        holder.delete.setOnClickListener {
            onClickListener.onClick(leaveRequests[position].id!!)
        }

        holder.edit.setOnClickListener {
            onClickListener.updateLeaveRequest(leaveRequests[position])
        }

    }

    override fun getItemCount(): Int {
        return leaveRequests.size;
    }

    fun setData(leaveRequestsData: ArrayList<LeaveRequestsItem>) {
        leaveRequests.clear()
        leaveRequests?.addAll(leaveRequestsData)
        notifyDataSetChanged()
    }

    class RequestsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var request_type = itemView.findViewById<View>(R.id.request_type) as TextView
        var metadata = itemView.findViewById<View>(R.id.metadata) as TextView
        var days = itemView.findViewById<View>(R.id.days) as TextView
        var from_to = itemView.findViewById<View>(R.id.from_to) as TextView
        var request_on = itemView.findViewById<View>(R.id.request_on) as TextView
        var status = itemView.findViewById<View>(R.id.status) as TextView
        var delete = itemView.findViewById<View>(R.id.delete) as ImageView
        var edit = itemView.findViewById<View>(R.id.edit) as ImageView
    }
}
