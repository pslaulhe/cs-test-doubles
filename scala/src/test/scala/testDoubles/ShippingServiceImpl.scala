package testDoubles

import com.example.{Purchase, ShippingService}

case class ShippingServiceImpl() extends ShippingService {
  var shippedCustomerEmailAddress = ""
  var shippedPurchase: Purchase = _

  override def ship(customerEmailAddress: String, purchase: Purchase): Unit = {
    shippedCustomerEmailAddress = customerEmailAddress
    shippedPurchase = purchase
  }
}
