package technology.dubaileading.maccessemployee.ui.payslip

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
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
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import technology.dubaileading.maccessemployee.BuildConfig
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.rest.entity.paysliplistmodel.DataX
import technology.dubaileading.maccessemployee.utility.SessionManager
import technology.dubaileading.maccessemployee.utility.hide
import java.nio.charset.Charset
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*


//class PayslipAdaptor {
//}
class PayslipAdaptor(private val context: Context, var dataList: List<DataX?>?) : RecyclerView.Adapter<PayslipAdaptor.AttendanceViewHolder>() {
    lateinit var bmp: Bitmap
    lateinit var scaledbmp: Bitmap

    // on below line we are creating a
    // constant code for runtime permissions.
    var PERMISSION_CODE = 101

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.payslipadaptorlayout, parent, false)
        bmp = BitmapFactory.decodeResource(context.resources, R.drawable.home_logo)
        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false)
        return AttendanceViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        val bean = dataList?.get(position)
     //   holder.paymonth.text = monthnamegh(bean!!.fromDate).toString()+"-"+monthnamegh(bean.toDate)
      //  holder.payamount.text = bean.amount
        holder.processedate.text = monthnamegh(bean!!.toDate)
       // holder.status.text = bean.status
        getVolley(bean.id.toString(),holder.webview,holder.row)
        try {
            val dob = bean!!.fromDate
            var month: String? = "0"
            var dd: String? = "0"
            var yer: String? = "0"
            try {

                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val d = sdf.parse(dob)
                val cal = Calendar.getInstance()
                cal.time = d
                val monthss:Int = checkDigit(cal[Calendar.MONTH] + 1)!!.toInt()
                val  monthname=getMonth(monthss)
                Log.e("dateconverter",monthname.toString())
                yer = checkDigit(cal[Calendar.YEAR])
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
           // this never ends while debugging
        } catch (e: Exception){
           e.printStackTrace() // this never gets called either
        }



        holder.row.setOnClickListener {
//            val intent = Intent(context, Payslipwebview::class.java)
//            intent.putExtra("Username",bean.id.toString())
//            context.startActivity(intent)
//            BaseApplication.QuestionObj.detailid=bean.id.toString()
            getVolley(bean.id.toString(), holder.webview,holder.row)

//            val download= context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
//            val PdfUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/online-learning-ea8c0.appspot.com/o/Uploads%2FSystem%20Design%20Basics%20Handbook%20-8.pdf?alt=media&token=084ec8ce-598a-4a14-ad7c-61003e509af6")
//            val getPdf = DownloadManager.Request(PdfUri)
//            getPdf.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//            download.enqueue(getPdf)
//            Toast.makeText(context,"Download Started", Toast.LENGTH_LONG).show()
        }

    }

    fun getMonth(month: Int): String? {
        return DateFormatSymbols().getMonths().get(month - 1)
    }
    override fun getItemCount(): Int {
        return dataList?.size!!
    }

    fun addList(items: List<DataX?>?){
        this.dataList = items
        notifyDataSetChanged()
    }

    fun monthnamegh(convertdate:String): CharSequence? {

            //val dob = bean!!.fromDate
            var month: String? = "0"
            var dd: String? = "0"
            var yer: String? = "0"


                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val d = sdf.parse(convertdate)
                val cal = Calendar.getInstance()
                cal.time = d
                val monthss:Int = checkDigit(cal[Calendar.MONTH] + 1)!!.toInt()
                   val year:Int =checkDigit(cal[Calendar.YEAR])!!.toInt()
                val  monthname=getMonth(monthss)+" "+year
                Log.e("dateconverter",monthname.toString())
                yer = checkDigit(cal[Calendar.YEAR])

        return monthname
    }

    fun getVolley(detailid: String, webview: WebView, row: LinearLayout)
    {
        val queue = Volley.newRequestQueue(context)
        // val url = "https://staging.dubaileading.technology/maccess-saas/api/public/api/admin/employeePayrollSlip?payroll_id=58&employee_id=8583"
        val url =
            BuildConfig.BASE_URL + "admin/employeePayrollSlip?payroll_id=" + detailid + "&employee_id=" + SessionManager.profileid.toString()

        val requestBody = ""
        val stringReq: StringRequest =
            object : StringRequest(
                Method.GET, url,
                Response.Listener { response ->

                    val mimeType = "text/html"
                    val encoding = "UTF-8"
                    webview.loadDataWithBaseURL("", response, mimeType, encoding, "")

                    row.setOnClickListener {
                        createWebPrintJob(webview)
                    }

//                    val converter: PdfConverter = PdfConverter.getInstance()
//                    val file =
//                        File(Environment.getExternalStorageDirectory().toString(), "file.pdf")
//                    val htmlString = "<html><body><p>WHITE (default)</p></body></html>"
//                    converter.convert(getContext(), htmlString, file)

                },
                Response.ErrorListener { error ->
                    Log.i("mylog", "error = " + error)
                }
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = "Bearer " + SessionManager.token


                    return headers
                }

                override fun getBody(): ByteArray {
                    return requestBody.toByteArray(Charset.defaultCharset())
                }
            }
        queue.add(stringReq)
    }


    private fun createWebPrintJob(webView: WebView) {

        val printManager = context
            .getSystemService(Context.PRINT_SERVICE) as PrintManager

        val printAdapter = webView.createPrintDocumentAdapter("MyDocument")

        val jobName = context.getString(R.string.app_name) + " Print Test"

        printManager.print(
            jobName, printAdapter,
            PrintAttributes.Builder().build()
        )
    }

    fun checkDigit(number: Int): String? {
        return if (number <= 9) "0$number" else number.toString()
    }
    class AttendanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var status = itemView.findViewById<View>(R.id.paystatus) as TextView
//        var processedate = itemView.findViewById<View>(R.id.processedate) as TextView
//        var paymonth = itemView.findViewById<View>(R.id.paymonth) as TextView
//        var payamount = itemView.findViewById<View>(R.id.payamount) as TextView
//        var row = itemView.findViewById<View>(R.id.paysliprow) as ConstraintLayout
          var processedate = itemView.findViewById<View>(R.id.processedate) as TextView
          var row = itemView.findViewById<View>(R.id.paysliprow) as LinearLayout
          var webview = itemView.findViewById<View>(R.id.web_view) as WebView


    }
}