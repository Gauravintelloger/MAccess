package technology.dubaileading.maccessuser.ui.attendance

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import technology.dubaileading.maccessuser.R
import technology.dubaileading.maccessuser.rest.entity.DataItem

class AttendanceReportAdapter( private val context: Context) : RecyclerView.Adapter<AttendanceReportAdapter.AttendanceViewHolder>() {

    private var dataList = ArrayList<DataItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_time_log, parent, false)

        return AttendanceViewHolder(view)
    }

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        holder.status.text = dataList[position].mode
        holder.dateTime.text = dataList[position].dateTimeUnix
        if (dataList[position]!!.remarks != null){
            holder.remark.text = dataList[position]!!.remarks.toString()
        }
        if (dataList[position].mode!!.equals("in")){
            holder.status_img.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_break_in))
        } else if (dataList[position].mode!!.equals("out")){
            holder.status_img.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_break_out))
        }

    }

    override fun getItemCount(): Int {
        return dataList.size;
    }

    fun addList(items: ArrayList<DataItem>){
        dataList.addAll(items)
        notifyDataSetChanged()
    }


    class AttendanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var status = itemView.findViewById<View>(R.id.status) as TextView
        var dateTime = itemView.findViewById<View>(R.id.dateTime) as TextView
        var status_img = itemView.findViewById<View>(R.id.status_img) as ImageView
        var remark = itemView.findViewById<View>(R.id.remark) as TextView
        var comp = itemView.findViewById<View>(R.id.comp) as TextView
    }
}