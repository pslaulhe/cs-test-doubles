package com.example

trait PurchaseOrderRepository {
  def save(purchase: Purchase): Purchase
}