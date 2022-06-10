package com.depromeet.baton.presentation.ui.mypage.model

import com.depromeet.baton.R
import com.depromeet.baton.domain.model.TicketSimpleInfo

data class SaleTicketItem (
   val typeId : Int,
   val date : String,
   val data : TicketSimpleInfo
)

sealed class SaleTicketListItem{
    abstract val ticket : SaleTicketItem
    abstract val layoutId: Int

    data class Header(
        override val ticket : SaleTicketItem,
        override val layoutId: Int = VIEW_TYPE
    ) :  SaleTicketListItem() {

        companion object {
            const val VIEW_TYPE = R.layout.item_ticket_sale_header
        }
    }

    data class Item(
        override val ticket : SaleTicketItem,
        override val layoutId: Int = VIEW_TYPE
    ) : SaleTicketListItem() {
        companion object {
            const val VIEW_TYPE = R.layout.item_ticket_sale
        }
    }

    data class PurchasedItem(
        override val ticket : SaleTicketItem,
        override val layoutId: Int = VIEW_TYPE
    ) : SaleTicketListItem() {
        companion object {
            const val VIEW_TYPE = R.layout.item_ticket_purchase
        }
    }

    data class Footer(
        override val ticket : SaleTicketItem,
        override val layoutId: Int = VIEW_TYPE
    ) : SaleTicketListItem() {
        companion object {
            const val VIEW_TYPE = R.layout.item_ticket_sale_footer
        }
    }
}
