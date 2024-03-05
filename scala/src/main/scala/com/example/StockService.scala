package com.example

trait StockService {
  def checkWhetherEnoughStock(productAndQuantityPairs: List[(String, Int)]): Boolean
}