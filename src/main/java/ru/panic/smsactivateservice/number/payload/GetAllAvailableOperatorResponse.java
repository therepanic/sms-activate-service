package ru.panic.smsactivateservice.number.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class GetAllAvailableOperatorResponse {
    private String status;
    @JsonProperty("countryOperators")
    private CountryOperator countryOperator;

    @Getter
    @Setter
    @Builder
    public static class CountryOperator {
        @JsonProperty("0")
        private List<String> russianCountryOperators;
    }
}
