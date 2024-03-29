package ru.panic.smsactivateservice.number.exception;

public class MerchantNotAuthorizedException extends RuntimeException {
    public MerchantNotAuthorizedException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
