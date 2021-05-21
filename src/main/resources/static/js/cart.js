/* Set rates + misc */
var taxRate = 0.05
var shippingRate = 50.0
var fadeTime = 300

var notyf = new Notyf()

document.querySelectorAll('.product-quantity input').forEach(element => {
  updateQuantity(element)
})

/* Assign actions */
$('.product-quantity input').change(function () {
  updateQuantity(this)
  let qty = this.value
  let id = this.dataset.itemid
  let path = `/cart/${id}/setquantity/${qty}`
  console.log(path);
  fetch(path)
    .then(resp => {
      if (resp.status == 200) {
        message = 'Changed item quantity'
        notyf.success(message)
      } else {
        message = 'Failed to change item quantity.<br />Please try again.'
        notyf.error(message)
      }
    })
    .catch(err => {
      message = 'Failed to change item quantity.<br />Please try again.'
      notyf.error(message)
    })
})

$('.product-removal button').click(function () {
  var itemId = this.value
  removeItemFromCart(itemId, this)
})

function removeItemFromCart (itemId, cartElement) {
  var path = `/removefromcart?id=${itemId}`
  var message
  fetch(path)
    .then(resp => {
      if (resp.status == 200) {
        message = 'Removed item from cart'
        notyf.success(message)
        removeItem(cartElement)
        checkIfCartIsEmpty()
      } else {
        message = 'Failed to remove item from cart.<br />Please try again.'
        notyf.error(message)
      }
    })
    .catch(err => {
      message = 'Failed to remove item from cart.<br />Please try again.'
      notyf.error(message)
    })
}

function checkIfCartIsEmpty () {
  var cartItemsCount = document.querySelectorAll('.product').length
  if (cartItemsCount == 0) {
    window.location.reload(true)
    console.log('EMPTY')
  }
}

/* Recalculate cart */
function recalculateCart () {
  console.log('Running totals')
  var subtotal = 0

  /* Sum up row totals */
  $('.product').each(function () {
    subtotal += parseFloat(
      $(this)
        .children('.product-line-price')
        .text()
    )
  })

  /* Calculate totals */
  var tax = subtotal * taxRate
  var shipping = subtotal > 0 ? shippingRate : 0
  var total = subtotal + tax + shipping

  /* Update totals display */
  $('.totals-value').fadeOut(fadeTime, function () {
    $('#cart-subtotal').html(subtotal.toFixed(2))
    $('#cart-tax').html(tax.toFixed(2))
    $('#cart-shipping').html(shipping.toFixed(2))
    $('#cart-total').html(total.toFixed(2))
    if (total == 0) {
      $('.checkout').fadeOut(fadeTime)
    } else {
      $('.checkout').fadeIn(fadeTime)
    }
    $('.totals-value').fadeIn(fadeTime)
  })
}

/* Update quantity */
function updateQuantity (quantityInput) {
  /* Calculate line price */
  var productRow = $(quantityInput)
    .parent()
    .parent()
  var price = parseFloat(productRow.children('.product-price').text())
  var quantity = $(quantityInput).val()
  var linePrice = price * quantity

  /* Update line price display and recalc cart totals */
  productRow.children('.product-line-price').each(function () {
    $(this).fadeOut(fadeTime, function () {
      $(this).text(linePrice.toFixed(2))
      recalculateCart()
      $(this).fadeIn(fadeTime)
    })
  })
}

/* Remove item from cart */
function removeItem (removeButton) {
  /* Remove row from DOM and recalc cart total */
  var productRow = $(removeButton)
    .parent()
    .parent()
  productRow.slideUp(fadeTime, function () {
    productRow.remove()
    recalculateCart()
  })
}
