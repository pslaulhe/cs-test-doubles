package testDoubles

import com.example.PurchaseOrderRepository

case class FakeOrderRepository() extends PurchaseOrderRepository {

  var savedPurchases: List[com.example.Purchase] = List()

  override def save(purchase: com.example.Purchase): com.example.Purchase = {
    savedPurchases = savedPurchases :+ purchase
    purchase
  }
}
