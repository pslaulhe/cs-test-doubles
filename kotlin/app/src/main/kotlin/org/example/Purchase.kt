package org.example

import java.util.UUID

data class Purchase(
    val id: UUID,
    val customerId: String,
    val productAndQuantityPairs: List<Pair<String, Int>>,
    val shippingAddress: String
)