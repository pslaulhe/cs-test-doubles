package com.example

trait InvoicingService {
  def issue(purchase: Purchase): Unit
}

