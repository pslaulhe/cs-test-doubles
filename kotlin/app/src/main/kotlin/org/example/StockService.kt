package org.example

import arrow.core.Either

interface StockService {
    fun checkWhetherEnoughStock(productAndQuantityPairs: List<Pair<String, Int>>): Boolean
}
