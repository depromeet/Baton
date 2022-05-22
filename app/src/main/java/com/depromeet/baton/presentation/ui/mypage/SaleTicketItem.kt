package com.depromeet.baton.presentation.ui.mypage

import com.depromeet.baton.R

data class SaleTicketItem (
    val shopName: String,
    val card: String,
    val price: String,
    val remainingDay: String,
    val place: String,
    val distance: String,
    val img: Int,
    val historyDate : String,
    val status: String
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
