package ru.panic.smsactivateservice.number.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GetAllServicePriceResponse {
    @JsonProperty("0")
    private CountryServicePrice russianCountryServicePrice;

    @Getter
    @Setter
    @Builder
    public static class ServicePrice {
        private int cost;
        private int count;
    }

    @Getter
    @Setter
    @Builder
    public static class CountryServicePrice {
        @JsonProperty("tg")
        private ServicePrice telegramServicePrice;
    }
}
