package com.fyrdev.monyia.adapters.driving.http.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationResponse {
    private String accessToken;
}
