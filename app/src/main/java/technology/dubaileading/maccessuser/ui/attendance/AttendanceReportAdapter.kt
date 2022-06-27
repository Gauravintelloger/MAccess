package technology.dubaileading.maccessuser.ui.attendance

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import technology.dubaileading.maccessuser.R
import technology.dubaileading.maccessuser.rest.entity.DataItem

class AttendanceReportAdapter : RecyclerView.Adapter<AttendanceReportAdapter.AttendanceViewHolder>() {

    private var dataList = ArrayList<DataItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_time_log, parent, false)

        return AttendanceViewHolder(view)
    }

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return dataList.size;
    }

    fun addList(items: ArrayList<DataItem>){
        dataList.addAll(items)
        notifyDataSetChanged()

    }

    class AttendanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var view = itemView.findViewById<View>(R.id.gap)
    }
}