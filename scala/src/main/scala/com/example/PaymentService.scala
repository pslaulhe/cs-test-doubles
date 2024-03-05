package com.example

trait PaymentService {
  def chargeCard(
                  cardNumber: String,
                  expirationYear: Int,
                  expirationMonth: Int,
                  cvv: String,
                  cardholderName: String,
                  amount: Double
                ): Unit
}
