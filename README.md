# test-doubles

**The input of the use case is:**

- A list of pairs [productId, quantity]
- The credit card information (number, expiration date, CVV, cardholder name)
- shipping address (country, ZIP code, street, number)
- customer email address
- customer id

**Acceptance criteria:**

The use case has to (not necessarily in this order):

- create and persist a purchase order (id, customerId, a list of productAndQuantity, shipping address).
- access the pricing system (another application of the company) to retrieve the price of a product (or a list of products).
- charge the customer credit card. You must use the API of a payment provider.
- notify the invoicing system (another application of the company) about the purchase, so it generates and send through email technology an invoice to the customer.
- send a shipping order to the carrier. You have to use their API.
- verify that there is enough stock for each product by querying the stock system (another application of the company)