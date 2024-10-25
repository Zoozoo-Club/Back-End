package Zoozoo.ZoozooClub.product.exception;

public class NotFoundProductsException extends RuntimeException {
    public NotFoundProductsException(String message) {
        super(message);
    }
}