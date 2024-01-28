package com.gamepricecomparator.common.web.response;

public record IsLoggedInResponse(String email, String nickname, String token, boolean success) {
}
