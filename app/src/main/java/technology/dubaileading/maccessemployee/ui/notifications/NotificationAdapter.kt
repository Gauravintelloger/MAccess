package technology.dubaileading.maccessemployee.ui.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.rest.entity.NotificationData


class NotificationAdapter : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

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

    }
}
