package ru.panic.smsactivateservice.number.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GetAllAvailableCountryResponse {
    @JsonProperty("0")
    private AvailableCountry russianCountry;

    @Getter
    @Setter
    @Builder
    public static class AvailableCountry {
        private int id;
        private String rus;
        private String eng;
        private String chn;
        private int visible;
        private int retry;
        private int rent;
        private int multiService;
    }
}
