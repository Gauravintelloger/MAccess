package technology.dubaileading.maccessemployee.ui.manager.Addnewjob

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import technology.dubaileading.maccessemployee.BaseApplication
import technology.dubaileading.maccessemployee.R
import technology.dubaileading.maccessemployee.databinding.PrereqquestionBinding
import technology.dubaileading.maccessemployee.rest.entity.prerequiistquestionmodel.Data

import technology.dubaileading.maccessemployee.ui.applyjobform.CheckboxAdaptor
import technology.dubaileading.maccessemployee.ui.applyjobform.Questiondetail
import java.lang.reflect.Type

//class AddnewjobpostAdaptor {
//}

class AddnewjobpostAdaptor(private val requireContext: Context) :
    RecyclerView.Adapter<AddnewjobpostAdaptor.ItemViewHolder>() {
    class ItemViewHolder(val binding: PrereqquestionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding: PrereqquestionBinding =
            PrereqquestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    private val differCallback = object : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean =
            oldItem.id == oldItem.id

    }
    private var onRowClickListener: ((Data) -> Unit)? = null
    val differ = AsyncListDiffer(this, differCallback)

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.binding.apply {
            holder.binding.checkbox.text =item.question

            row.setOnClickListener{
                onRowClickListener?.invoke(item)
            }


        }



    }

    fun onRowClickListener(listener: ((Data) -> Unit)) {
        onRowClickListener = listener
    }

    override fun getItemCount(): Int = differ.currentList.size
}