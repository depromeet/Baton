package com.depromeet.baton.presentation.ui.mypage.model

import com.depromeet.baton.R

sealed class ProfileIconItem :Cloneable {
    abstract val icon : ProfileIcon?
    abstract val layoutId: Int
    abstract var isChecked : Boolean
    public override fun clone(): ProfileIconItem {
        return super.clone() as ProfileIconItem
    }

    data class EmotionIcon(
        override val icon : ProfileIcon?,
        override val layoutId: Int = VIEW_TYPE,
        override var isChecked :Boolean =false
    ) : ProfileIconItem(),Cloneable {
        companion object {
            const val VIEW_TYPE = R.layout.item_profile_icon
        }

        override fun clone(): ProfileIconItem {
            return super<ProfileIconItem>.clone()
        }
    }

    data class CameraIcon(
        override val icon : ProfileIcon?= null,
        override val layoutId: Int = VIEW_TYPE,
        override var isChecked :Boolean =false
    ) : ProfileIconItem(), Cloneable {
        companion object {
            const val VIEW_TYPE = R.layout.item_profile_camera
        }
        override fun clone(): ProfileIconItem {
            return super<ProfileIconItem>.clone()
        }
    }

}