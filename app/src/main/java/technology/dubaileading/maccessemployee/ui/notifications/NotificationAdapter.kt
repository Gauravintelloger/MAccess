package technology.dubaileading.maccessemployee.ui.notifications

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.rest.entity.NotificationData


class NotificationAdapter(var context: Context) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    private var dataList = ArrayList<NotificationData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationAdapter.NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notification_holder, parent, false)

        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationAdapter.NotificationViewHolder, position: Int) {
        holder.notification.text = dataList[position].title
        holder.message.text = dataList[position].message
        holder.time.text = dataList[position].dateOfNotificationWithTime
        if (dataList[position].remarks != null){
            holder.remarks.text = dataList[position].remarks
            holder.remarks.visibility = View.VISIBLE
        }else{
            holder.remarks.visibility = View.GONE
        }

        if (dataList[position].type.equals("Document Request")){
            holder.image.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.document_notification))
        } else if (dataList[position].type.equals("Leave Application")){
            holder.image.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.leave_notification))
        } else {
            holder.image.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_insurance_1))
        }


    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun addList(notificationData : ArrayList<NotificationData>){
        dataList.clear()
        dataList.addAll(notificationData)
        notifyDataSetChanged()
    }

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var notification = itemView.findViewById<View>(R.id.notification) as TextView
        var message = itemView.findViewById<View>(R.id.message) as TextView
        var time = itemView.findViewById<View>(R.id.time) as TextView
        var remarks = itemView.findViewById<View>(R.id.remarks) as TextView
        var image = itemView.findViewById<View>(R.id.image) as ImageView

    }
}
