package com.jaswine.transactional_app.controller.dto;

import lombok.Data;

@Data
public class ReplenishmentRequestDto {
    private Float amount;
    private String externalSource;
    private String externalReference;
}
