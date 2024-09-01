package com.araouf.server.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeneralResponse {

    @JsonProperty("message")
    private String msg;

    @JsonProperty("status")
    private String status;

    @JsonProperty("statusCode")
    private Integer statusCode;
}
