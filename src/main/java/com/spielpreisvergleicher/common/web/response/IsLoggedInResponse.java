package com.spielpreisvergleicher.common.web.response;

public record IsLoggedInResponse(String email, String nickname, boolean success) {
}
