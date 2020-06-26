package kakaopay.spray.service;

import kakaopay.spray.entity.Token;

public interface TokenService {
    Token generateToken();
    boolean checkExpired(String createdAt, int expire);
}
