package technology.dubaileading.maccessemployee.ui.requests

import technology.dubaileading.maccessemployee.rest.entity.OtherRequestsItem

interface DocumentClickListener{
    fun updateDocRequest(otherRequestsItem: OtherRequestsItem)

    fun onDeleteClick(id: Int)

    fun downloadDoc(otherRequestsItem: OtherRequestsItem)
}