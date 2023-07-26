package technology.dubaileading.maccessemployee.ui.manager

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import technology.dubaileading.maccessemployee.BaseApplication
import technology.dubaileading.maccessemployee.databinding.JobtitleviewBinding
import technology.dubaileading.maccessemployee.rest.entity.jobpostlistresponse.DataXX
import technology.dubaileading.maccessemployee.rest.entity.managerjobpostrequestlist.Data
import technology.dubaileading.maccessemployee.rest.entity.managerjobpostrequestlist.DataX
import technology.dubaileading.maccessemployee.ui.jobpost.Jobpostlistviewmodel
import technology.dubaileading.maccessemployee.ui.jobpostdetail.Jobpostdetail

//class ManagerJobPostAdaptor {
//}
class ManagerJobPostAdaptor(var context: Context, var jobpostlistviewmodel: Jobpostlistviewmodel) :
    PagingDataAdapter<DataX, ManagerJobPostAdaptor.ItemViewHolder>(ItemDifferCallback()) {
    class ItemViewHolder(val binding: JobtitleviewBinding) : RecyclerView.ViewHolder(binding.root)

    var productMarker: String = ""
    var selectedItem: Int = -0
    var market = ""

    var devicetoken = ""

    class ItemDifferCallback : DiffUtil.ItemCallback<DataX>() {
        override fun areItemsTheSame(
            oldItem: DataX,
            newItem: DataX
        ): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(
            oldItem: DataX,
            newItem: DataX
        ): Boolean =
            oldItem.id == newItem.id

    }

    private var onRowClickListener: ((Data) -> Unit)? = null
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)


        holder.binding.jobPostName.setText(item!!.jobPostName)
        holder.binding.jobCategory.setText(item.companyName)
        holder.binding.jobLocation.setText(item.branchName)
        holder.binding.departmentName.setText(item.departmentName)
        holder.binding.experience.visibility=View.GONE
      //  holder.binding.experience.setText(item.minExperience+" - "+item.maxExperience +"Years")
        holder.binding.status.setText(item.jobRequestStatus)

        holder.binding.row.setOnClickListener {
            val intent = Intent(context, Managerjobdetails::class.java)
            intent.putExtra("Username",item.id.toString())
            context.startActivity(intent)
            BaseApplication.QuestionObj.detailid=item.id.toString()

        }



    }

    fun onRowClickListener(listener: ((Data) -> Unit)) {
        onRowClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val findNavController: NavController
        val binding =
            JobtitleviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)


        return ItemViewHolder(binding)
    }


}