package org.example

interface PurchaseOrderRepository {
    fun save(purchase: Purchase): Purchase
}