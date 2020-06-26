package kakaopay.spray.service;

import kakaopay.spray.entity.Token;

import java.math.BigInteger;

public interface TokenService {
    Token generateToken();
}
