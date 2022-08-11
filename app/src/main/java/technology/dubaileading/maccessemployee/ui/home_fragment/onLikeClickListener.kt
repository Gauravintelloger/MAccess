package technology.dubaileading.maccessemployee.ui.home_fragment

import technology.dubaileading.maccessemployee.rest.entity.PostData

interface onLikeClickListener {
    fun onLikeClick(postData: PostData, position: Int)
}