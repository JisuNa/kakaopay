package kakaopay.spray.service.Impl;

import kakaopay.spray.entity.Token;
import kakaopay.spray.persistence.TokenRepository;
import kakaopay.spray.service.TokenService;
import kakaopay.spray.util.TokenUtil;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class TokenServiceImpl implements TokenService {

    private TokenRepository tokenRepository;

    public TokenServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public Token generateToken() {
        Token token = new Token();
        token.setToken(TokenUtil.getSprayToken());
        return tokenRepository.save(token);
    }
}
