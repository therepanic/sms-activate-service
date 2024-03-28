package ru.panic.smsactivateservice.number.in;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.panic.smsactivateservice.number.dto.NumberDto;
import ru.panic.smsactivateservice.number.dto.SmsDto;
import ru.panic.smsactivateservice.number.model.NumberActivationOrder;
import ru.panic.smsactivateservice.number.model.type.NumberActivationOrderStatus;
import ru.panic.smsactivateservice.number.model.type.NumberService;
import ru.panic.smsactivateservice.number.payload.CreateNumberSmsRequest;
import ru.panic.smsactivateservice.number.service.impl.NumberBaseService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/number")
@RequiredArgsConstructor
public class NumberController {

    private final NumberBaseService numberBaseService;

    @GetMapping("/getWithLastSeenUpdate")
    public NumberDto get(@RequestParam("id") long id) {
        return numberBaseService.getWithLastSeenUpdate(id);
    }

    @GetMapping("/activationOrder/getAll")
    public List<NumberActivationOrder> getAllActivationOrder() {
        return numberBaseService.getAllActivationOrder();
    }

    @PostMapping("/sms")
    public SmsDto createSms(@RequestBody CreateNumberSmsRequest createNumberSmsRequest) {
        return numberBaseService.createSms(createNumberSmsRequest);
    }

    @PatchMapping("/activationOrder/updateStatus")
    public void updateActivationOrderStatus(@RequestParam("id") long id,
                                            @RequestParam("status") NumberActivationOrderStatus status) {
        numberBaseService.updateActivationOrderStatus(id, status);
    }

    @PatchMapping("/activationOrder/updatePhoneNumber")
    public void updateActivationOrderPhoneNumber(@RequestParam("id") long id,
                                                 @RequestParam("phoneNumber") String phoneNumber) {
        numberBaseService.updateActivationOrderPhoneNumber(id, phoneNumber);
    }

}
