package ru.panic.smsactivateservice.number.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.panic.smsactivateservice.number.repository.MerchantRepository;

@Component
@RequiredArgsConstructor
public class MerchantSecurity {

    private final MerchantRepository merchantRepository;

    public void secureByApiKey(String apiKey) {
        if (!merchantRepository.existsByApiKey(apiKey)) {
            throw new RuntimeException();
        }
    }
}
