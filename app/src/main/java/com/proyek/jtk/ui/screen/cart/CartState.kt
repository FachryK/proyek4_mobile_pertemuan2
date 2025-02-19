package com.proyek.jtk.ui.screen.cart

import com.proyek.jtk.model.OrderReward

data class CartState(
    val orderReward: List<OrderReward>,
    val totalRequiredPoint: Int
)