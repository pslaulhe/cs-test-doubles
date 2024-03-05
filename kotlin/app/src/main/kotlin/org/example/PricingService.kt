package org.example

interface PricingService {
    fun getPrices(productIds: List<String>): List<Pair<String, Double>>
}
