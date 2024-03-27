package ru.panic.smsactivateservice.number.in;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.panic.smsactivateservice.number.model.type.NumberService;
import ru.panic.smsactivateservice.number.service.impl.NumberBaseService;

@RestController
@RequestMapping("/stubs/handler_api.php")
@RequiredArgsConstructor
public class NumberStub {

    private final NumberBaseService numberBaseService;

    @PostMapping
    public Object activateNumber(
            @RequestParam(value = "action", required = false) String action,
            @RequestParam(value = "service", required = false) NumberService service,
            @RequestParam(value = "country", required = false) String country
            ) {

        switch (action) {
            case "getNumberV2" -> {
                return numberBaseService.handleActivate(service,
                        country);
            }
        }

        return null;
    }
}
