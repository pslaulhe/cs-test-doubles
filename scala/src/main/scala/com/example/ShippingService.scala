package com.example

trait ShippingService {
  def ship(customerEmailAddress: String, purchase: Purchase): Unit
}

