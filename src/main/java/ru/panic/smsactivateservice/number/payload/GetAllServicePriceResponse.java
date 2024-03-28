package ru.panic.smsactivateservice.number.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GetAllServicePriceResponse {
    @JsonProperty("tg")
    private TelegramServicePrice telegramServicePrice;

    @Getter
    @Setter
    @Builder
    public static class TelegramServicePrice {
        @JsonProperty("0")
        private CountryServicePrice russianCountryServicePrice;
    }

    @Getter
    @Setter
    @Builder
    public static class CountryServicePrice {
        private int count;
        private int price;
        @JsonProperty("retail_price")
        private int retailPrice;
    }
}
