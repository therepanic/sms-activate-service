package ru.panic.smsactivateservice.number.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.panic.smsactivateservice.number.dto.NumberStubExceptionDto;
import ru.panic.smsactivateservice.number.exception.MerchantNotAuthorizedException;

@RestControllerAdvice
public class NumberStubAdvancedHandler {
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(MerchantNotAuthorizedException.class)
    public NumberStubExceptionDto handleMerchantNotAuthorizedException(MerchantNotAuthorizedException merchantNotAuthorizedException) {
        return NumberStubExceptionDto.builder()
                .exceptionMessage(merchantNotAuthorizedException.getMessage())
                .build();
    }
}
