import com.example.{ConfirmCheckout, InvoicingService, PaymentService, PricingService, Purchase, PurchaseOrderRepository, ShippingService, StockService, UUIDGenerator}
import org.mockito.AdditionalAnswers
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{mock, times, verify, when}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import testDoubles.ShippingServiceImpl

class ConfirmCheckoutSpec extends AnyWordSpec with Matchers {
  "ConfirmCheckout" should {

    "test execute happy path" in {
      val uuidGenerator:UUIDGenerator = new UUIDGenerator() // no sense to double this one...
      val stockService: StockService = mock(classOf[StockService]) // spy
      when(stockService.checkWhetherEnoughStock(any())).thenReturn(true)

      val purchaseOrderRepository: PurchaseOrderRepository = mock(classOf[PurchaseOrderRepository]) // spy
      when(purchaseOrderRepository.save(any())).thenAnswer(AdditionalAnswers.returnsFirstArg())

      val pricingService: PricingService= mock(classOf[PricingService]) // spy
      when(pricingService.getPrices(any())).thenReturn(List(("1", 100.0)))

      val paymentService: PaymentService = mock(classOf[PaymentService]) // Mock
      val invoicingService: InvoicingService= mock(classOf[InvoicingService]) // Mock
      val shippingService: ShippingServiceImpl = ShippingServiceImpl() // Fake & Stub

      val confirmCheckoutUseCase = new ConfirmCheckout(uuidGenerator, purchaseOrderRepository, pricingService, paymentService, invoicingService, shippingService, stockService)

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

      verify(purchaseOrderRepository, times(1)).save(any())
    }

    "test not enough stock" in {

    }

    "test total price <= 0" in {

    }
  }
}
