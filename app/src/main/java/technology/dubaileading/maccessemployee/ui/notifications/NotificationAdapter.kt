package technology.dubaileading.maccessemployee.ui.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import technology.dubaileading.maccessemployee.R


class NotificationAdapter : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationAdapter.NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notification_holder, parent, false)

        return NotificationAdapter.NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationAdapter.NotificationViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 4;
    }

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}
