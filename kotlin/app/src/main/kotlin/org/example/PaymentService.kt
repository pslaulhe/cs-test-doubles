package org.example

interface PaymentService {
    fun chargeCard(
        cardNumber: String,
        expirationYear: Int,
        expirationMonth: Int,
        cvv: String,
        cardholderName: String,
        amount: Double
    )
}