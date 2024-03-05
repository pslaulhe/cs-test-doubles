package org.example


class ConfirmCheckout(
    private val uuidGenerator: UUIDGenerator,
    private val purchaseOrderRepository: PurchaseOrderRepository,
    private val pricingService: PricingService,
    private val paymentService: PaymentService,
    private val invoicingService: InvoicingService,
    private val shippingService: ShippingService,
    private val stockService: StockService
) {

    fun execute(
        productAndQuantityPairs: List<Pair<String, Int>>,
        cardNumber: String,
        expirationYear: Int,
        expirationMonth: Int,
        cvv: String,
        cardholderName: String,
        shippingAddress: String,
        customerEmailAddress: String,
        customerId: String
    ) {
        val thereIsEnoughStock = stockService.checkWhetherEnoughStock(productAndQuantityPairs)

        if(!thereIsEnoughStock) throw RuntimeException("Not enough stock")

        val purchaseId = uuidGenerator.randomUUID()

        val purchase = Purchase(
            id = purchaseId,
            customerId = customerId,
            productAndQuantityPairs = productAndQuantityPairs,
            shippingAddress = shippingAddress
        )

        val savedPurchase = purchaseOrderRepository.save(purchase = purchase)

        val productIds: List<String> = productAndQuantityPairs.map { it.first }

        val productIdAndPricePairs: List<Pair<String, Double>> = pricingService.getPrices(productIds)

        val amountToCharge = productIdAndPricePairs.sumOf { productIdAndPrice ->
            val productId = productIdAndPrice.first
            val price = productIdAndPrice.second
            val productAndQuantity: Pair<String, Int> = productAndQuantityPairs.first { it.first == productId }
            val quantity = productAndQuantity.second
            quantity * price
        }

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
