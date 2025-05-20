package ru.panic.smsactivateservice.number.in;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.panic.smsactivateservice.number.dto.SmsDto;
import ru.panic.smsactivateservice.number.model.Number;
import ru.panic.smsactivateservice.number.payload.CreateNumberSmsRequest;
import ru.panic.smsactivateservice.number.payload.GetStatusWithLastSeenUpdateResponse;
import ru.panic.smsactivateservice.number.service.impl.NumberBaseService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/number")
@RequiredArgsConstructor
public class NumberController {

    private final NumberBaseService numberBaseService;

    @GetMapping("/getStatusWithLastSeenUpdate")
    public GetStatusWithLastSeenUpdateResponse getStatusWithLastSeenUpdate(@RequestParam("id") long id) {
        return numberBaseService.getStatusWithLastSeenUpdate(id);
    }

    @GetMapping("/smsOrder/getRandomSmsOrderExactly")
    public String getRandomSmsOrderExactly() {
        return numberBaseService.getRandomSmsOrderExactly();
    }

    @GetMapping("/activationOrder/getWorking")
    public List<Object> getWorking() {
        return numberBaseService.getWorking();
    }

    @PostMapping("/sms")
    public SmsDto createSms(@RequestBody CreateNumberSmsRequest createNumberSmsRequest) {
        return numberBaseService.createSms(createNumberSmsRequest);
    }

    @PatchMapping("/smsOrder/updateSms")
    public void updateSmsOrderSms(@RequestParam("id") long id,
                                  @RequestParam("sms") String sms,
                                  @RequestParam("status") String status) {
        numberBaseService.updateSmsOrderSms(id, sms, status);
    }

    @PostMapping("/activationOrder/all")
    public void createAllActivationOrder(@RequestBody List<String> phoneNumbers) {
        numberBaseService.createAllActivationOrder(phoneNumbers);
    }

}
