package ru.panic.smsactivateservice.number.in;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.panic.smsactivateservice.number.dto.NumberDto;
import ru.panic.smsactivateservice.number.dto.SmsDto;
import ru.panic.smsactivateservice.number.model.NumberActivationOrder;
import ru.panic.smsactivateservice.number.model.type.NumberActivationOrderStatus;
import ru.panic.smsactivateservice.number.model.type.NumberService;
import ru.panic.smsactivateservice.number.payload.CreateNumberSmsRequest;
import ru.panic.smsactivateservice.number.payload.GetStatusWithLastSeenUpdateResponse;
import ru.panic.smsactivateservice.number.payload.UpdateActivationOrderPhoneNumberResponse;
import ru.panic.smsactivateservice.number.service.impl.NumberBaseService;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/number")
@RequiredArgsConstructor
public class NumberController {

    private final NumberBaseService numberBaseService;

    @GetMapping("/getStatusWithLastSeenUpdate")
    public GetStatusWithLastSeenUpdateResponse getStatusWithLastSeenUpdate(@RequestParam("id") long id) {
        return numberBaseService.getStatusWithLastSeenUpdate(id);
    }

    @GetMapping("/activationOrder/getAll")
    public List<NumberActivationOrder> getAllActivationOrder() {
        return numberBaseService.getAllActivationOrder();
    }

    @GetMapping("/activationOrder/getRandomExactly")
    public Object getRandomActivationOrderExactly() {
        return numberBaseService.getRandomActivationOrderExactly();
    }

    @PostMapping("/sms")
    public SmsDto createSms(@RequestBody CreateNumberSmsRequest createNumberSmsRequest) {
        return numberBaseService.createSms(createNumberSmsRequest);
    }

    @PatchMapping("/activationOrder/updatePhoneNumber")
    public UpdateActivationOrderPhoneNumberResponse updateActivationOrderPhoneNumber(@RequestParam("id") long id,
                                                                                     @RequestParam("phoneNumber") String phoneNumber) {
        return numberBaseService.updateActivationOrderPhoneNumber(id, phoneNumber);
    }

}
