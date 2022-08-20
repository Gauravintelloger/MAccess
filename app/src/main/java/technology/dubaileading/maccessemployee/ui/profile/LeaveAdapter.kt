package technology.dubaileading.maccessemployee.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.rest.entity.LeaveDataItem


class LeaveAdapter : RecyclerView.Adapter<LeaveAdapter.LeaveViewHolder>() {
    private var dataList = ArrayList<LeaveDataItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaveViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.leave_type_holder, parent, false)
        return LeaveViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeaveViewHolder, position: Int) {
        holder.leaveLeft_Heading.text = dataList[position].leaveCode+" Left"
        holder.leaveLeft.text = dataList[position].remainingLeaves.toString()
        holder.leaveTotal.text = dataList[position].totalLeaves.toString()

    }

    override fun getItemCount(): Int {
        return dataList.size;
    }

    fun addList(leaveDataItemList : ArrayList<LeaveDataItem>){
        dataList.clear()
        dataList.addAll(leaveDataItemList)
        notifyDataSetChanged()
    }

    class LeaveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var leaveLeft_Heading = itemView.findViewById<View>(R.id.leaveLeft_Heading) as TextView
        var leaveLeft = itemView.findViewById<View>(R.id.leaveLeft) as TextView
        var leaveTotal = itemView.findViewById<View>(R.id.leaveTotal) as TextView

    }
}
