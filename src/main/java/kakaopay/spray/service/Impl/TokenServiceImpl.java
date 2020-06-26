package kakaopay.spray.service.Impl;

import kakaopay.spray.entity.Token;
import kakaopay.spray.persistence.TokenRepository;
import kakaopay.spray.service.TokenService;
import kakaopay.spray.util.DateUtil;
import kakaopay.spray.util.TokenUtil;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    @Override
    public boolean checkExpired(String createdAt, int expire) {
        boolean rtn;

        try {
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date generatedTime = transFormat.parse(createdAt);
            String now = DateUtil.getNow();
            Date nowTime = transFormat.parse(now);

            long minGap = (nowTime.getTime() - generatedTime.getTime()) / 60000;
            if(minGap > expire) throw new Exception();

            rtn = false;
        } catch(Exception e) {
            rtn = true;
        }

        return rtn;
    }
}
