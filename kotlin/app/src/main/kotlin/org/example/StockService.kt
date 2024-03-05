package org.example

interface StockService {
    fun checkWhetherEnoughStock(productAndQuantityPairs: List<Pair<String, Int>>): Boolean
}
