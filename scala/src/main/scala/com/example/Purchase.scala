package com.example

import java.util.UUID

case class Purchase(
                     id: UUID,
                     customerId: String,
                     productAndQuantityPairs: List[(String, Int)],
                     shippingAddress: String
                   )
