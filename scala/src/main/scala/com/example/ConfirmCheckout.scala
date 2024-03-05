package com.example

class ConfirmCheckout(
                       uuidGenerator: UUIDGenerator,
                       purchaseOrderRepository: PurchaseOrderRepository,
                       pricingService: PricingService,
                       paymentService: PaymentService,
                       invoicingService: InvoicingService,
                       shippingService: ShippingService,
                       stockService: StockService
                     ) {

  def execute(
               productAndQuantityPairs: List[(String, Int)],
               cardNumber: String,
               expirationYear: Int,
               expirationMonth: Int,
               cvv: String,
               cardholderName: String,
               shippingAddress: String,
               customerEmailAddress: String,
               customerId: String
             ): Unit = {
    val thereIsEnoughStock = stockService.checkWhetherEnoughStock(productAndQuantityPairs)

    if (!thereIsEnoughStock) throw new RuntimeException("Not enough stock")

    val purchaseId = uuidGenerator.randomUUID()

    val purchase = Purchase(
      id = purchaseId,
      customerId = customerId,
      productAndQuantityPairs = productAndQuantityPairs,
      shippingAddress = shippingAddress
    )

    val savedPurchase = purchaseOrderRepository.save(purchase)

    val productIds: List[String] = productAndQuantityPairs.map(_._1)

    val productIdAndPricePairs: List[(String, Double)] = pricingService.getPrices(productIds)

    val amountToCharge = productIdAndPricePairs.map { case (productId, price) =>
      val quantity = productAndQuantityPairs.find(_._1 == productId).map(_._2).getOrElse(0)
      quantity * price
    }.sum

    paymentService.chargeCard(
      cardNumber = cardNumber,
      expirationYear = expirationYear,
      expirationMonth = expirationMonth,
      cvv = cvv,
      cardholderName = cardholderName,
      amount = amountToCharge
    )

    invoicingService.issue(savedPurchase)

    shippingService.ship(customerEmailAddress, savedPurchase)
  }
}
