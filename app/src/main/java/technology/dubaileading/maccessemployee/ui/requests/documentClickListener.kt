package technology.dubaileading.maccessemployee.ui.requests

import technology.dubaileading.maccessemployee.rest.entity.OtherRequestsItem

interface documentClickListener{
    fun updateDocRequest(otherRequestsItem: OtherRequestsItem)

    fun onClick(id: Int)

    fun downloadDoc(otherRequestsItem: OtherRequestsItem)
}