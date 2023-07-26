package technology.dubaileading.maccessemployee.ui.timecreaditrequest

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.w3c.dom.Text
import technology.dubaileading.maccessemployee.BaseApplication
import technology.dubaileading.maccessemployee.BuildConfig
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.rest.entity.paysliplistmodel.DataX
import technology.dubaileading.maccessemployee.rest.entity.timecreditrequestlist.Data
import technology.dubaileading.maccessemployee.rest.entity.timecreditrequestlist.Employee
import technology.dubaileading.maccessemployee.ui.interviewround.DetailInterviewlist
import technology.dubaileading.maccessemployee.ui.payslip.PayslipAdaptor
import technology.dubaileading.maccessemployee.ui.requests.LeaveClickListener
import technology.dubaileading.maccessemployee.ui.timecreaditrequest.timecreditlistdetail.Timecreditlistdetail
import technology.dubaileading.maccessemployee.utility.SessionManager
import technology.dubaileading.maccessemployee.utils.CustomDialog
import java.nio.charset.Charset
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

//class TimeCreditlistAdaptor {
//}
class TimeCreditlistAdaptor(private val context: Context, val onClickListener: Creditlistclick,
                            var dataList: List<Data?>?) : RecyclerView.Adapter<TimeCreditlistAdaptor.AttendanceViewHolder>() {
    lateinit var bmp: Bitmap
    lateinit var scaledbmp: Bitmap

    // on below line we are creating a
    // constant code for runtime permissions.
    var PERMISSION_CODE = 101

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.timecreditlistview, parent, false)

        return AttendanceViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        val bean = dataList?.get(position)


        holder.creaditname.text=bean!!.employee.name
        holder.creditdate.text= bean.date
        holder.usagetype.text= bean.usageType.replace("_"," ")
        holder.creditlimit.text= bean.creditsRequiredInMins.toString()
        holder.creditstatus.text= bean.status


        if (bean.status.equals("pending")) {
            holder.creditstatus.background = ContextCompat.getDrawable(context, R.drawable.pending_bg)
            holder.creditstatus.setTextColor(context.resources.getColor(R.color.text_color_yellow))

            holder.delete.visibility = View.VISIBLE
            //holder.edit.visibility = View.VISIBLE
        } else if (bean.status.equals("approved")) {
            holder.creditstatus.background = ContextCompat.getDrawable(context, R.drawable.approved_bg)
            holder.creditstatus.setTextColor(context.resources.getColor(R.color.text_color_green))

            holder.delete.visibility = View.GONE
            //holder.edit.visibility = View.GONE
        } else if (bean.status.equals("rejected")) {
            holder.creditstatus.background = ContextCompat.getDrawable(context, R.drawable.declined_bg)
            holder.creditstatus.setTextColor(context.resources.getColor(R.color.text_color_red))
            holder.delete.visibility = View.GONE

        }

//        if (bean.status=="approved")
//        {
//            holder.delete.visibility=View.GONE
//        }
//
        holder.delete.setOnClickListener {
            onClickListener.onDeleteClicked(bean.id!!)

        }

        holder.row.setOnClickListener {
            val intent = Intent(context, Timecreditlistdetail::class.java)
            intent.putExtra("Username",bean.id.toString())
            context.startActivity(intent)
            BaseApplication.QuestionObj.detailid=bean.id.toString()
        }


    }





    fun getMonth(month: Int): String? {
        return DateFormatSymbols().getMonths().get(month - 1)
    }
    override fun getItemCount(): Int {
        return dataList?.size!!
    }

    fun addList(items: List<Data?>?){
        this.dataList = items
        notifyDataSetChanged()
    }


    class AttendanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var creaditname = itemView.findViewById<View>(R.id.creaditname) as TextView
        var creditlimit = itemView.findViewById<View>(R.id.creditlimit) as TextView
        var usagetype = itemView.findViewById<View>(R.id.usagetype) as TextView
        var creditdate = itemView.findViewById<View>(R.id.creditdate) as TextView
        var creditstatus = itemView.findViewById<View>(R.id.creditstatus) as TextView
        var delete = itemView.findViewById<View>(R.id.deleterequest) as LinearLayout
        var row = itemView.findViewById<View>(R.id.row) as LinearLayout


    }
}