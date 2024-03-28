package ru.panic.smsactivateservice.number.model.type;

public enum NumberActivationState {
    ACCESS_READY,
    ACCESS_RETRY_GET,
    STATUS_WAIT_CODE,
    STATUS_WAIT_RESEND,
    STATUS_OK
}
