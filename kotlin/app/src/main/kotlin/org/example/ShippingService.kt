package org.example

interface ShippingService {
    fun ship(customerEmailAddress: String, purchase: Purchase)
}