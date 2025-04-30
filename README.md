# Product Cart Behavior

## Single Item in Stock

If a product has only one unit in stock, the cart quantity is not increased. The system will continue with the existing quantity without trying to increase it.

## Known Bug

There is a bug when you update the amount of a product and suddenly remove it from the cart. The cart gets bugged and shows the product with the updated amount, even though the product was removed.
