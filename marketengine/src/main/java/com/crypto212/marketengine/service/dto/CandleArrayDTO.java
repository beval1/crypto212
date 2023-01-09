package com.crypto212.marketengine.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CandleArrayDTO {
    List<CandleDTO> candles;
    String startTime;
    String endTime;
    String interval;
    String asset;
}
