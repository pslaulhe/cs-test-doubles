import com.example._
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{mock, when}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import testDoubles.{FakeOrderRepository, ShippingServiceImpl}

class ConfirmCheckoutSpec extends AnyWordSpec with Matchers {
  "ConfirmCheckout" should {
    val uuidGenerator:UUIDGenerator = new UUIDGenerator() // no sense to double this one...
    val stockService: StockService = mock(classOf[StockService]) // mock
    val purchaseOrderRepository: PurchaseOrderRepository = FakeOrderRepository() // fake

    val pricingService: PricingService = mock(classOf[PricingService]) // mock
    val paymentService: PaymentService = mock(classOf[PaymentService]) // mock
    val invoicingService: InvoicingService = mock(classOf[InvoicingService]) // Mock
    val shippingService: ShippingServiceImpl = ShippingServiceImpl() // Stub & Spy

    val confirmCheckoutUseCase = new ConfirmCheckout(uuidGenerator, purchaseOrderRepository, pricingService, paymentService, invoicingService, shippingService, stockService)

    "test execute happy path" in {
      // Set up
      when(stockService.checkWhetherEnoughStock(any())).thenReturn(true)
      when(pricingService.getPrices(any())).thenReturn(List(("1", 100.0)))

      // Arrange
      val productAndQuantityPairs = List(("1", 1))
      val cardNumber = "123"
      val expirationYear = 2024
      val expirationMonth = 4
      val cvv = "123"
      val cardholderName = "Pablo"
      val shippingAddress = "streetSFOJ"
      val customerEmailAddress = "testEmail"
      val customerId = "1"

      // Act
      confirmCheckoutUseCase.execute(productAndQuantityPairs, cardNumber, expirationYear, expirationMonth, cvv, cardholderName, shippingAddress, customerEmailAddress, customerId)

      // Assert
      shippingService.shippedCustomerEmailAddress should be(customerEmailAddress)
      shippingService.shippedPurchase.shippingAddress should be(shippingAddress)
      shippingService.shippedPurchase.productAndQuantityPairs should be(productAndQuantityPairs)
      shippingService.shippedPurchase.customerId should be(customerId)
    }

    "test not enough stock" in {
      // Most of the dependencies are dummies in this test
      when(stockService.checkWhetherEnoughStock(any())).thenReturn(false)

      // Arrange
      val productAndQuantityPairs = List(("1", 1))
      val cardNumber = "123"
      val expirationYear = 2024
      val expirationMonth = 4
      val cvv = "123"
      val cardholderName = "Pablo"
      val shippingAddress = "streetSFOJ"
      val customerEmailAddress = "testEmail"
      val customerId = "1"

      // Act
      assertThrows[RuntimeException](confirmCheckoutUseCase.execute(productAndQuantityPairs, cardNumber, expirationYear, expirationMonth, cvv, cardholderName, shippingAddress, customerEmailAddress, customerId))
    }
  }
}
