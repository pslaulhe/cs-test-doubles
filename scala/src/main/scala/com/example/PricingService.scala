package com.example

trait PricingService {
  def getPrices(productIds: List[String]): List[(String, Double)]
}
