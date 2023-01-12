package com.crypto212.marketengine.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AvgPriceWrapper {
    private AvgPrice data;
    @JsonProperty("x-mbx-used-weight")
    private String usedWeight;
    @JsonProperty("x-mbx-used-weight-1m")
    private String usedWeight1m;
}
