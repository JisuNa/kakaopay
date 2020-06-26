package kakaopay.spray.persistence;

import kakaopay.spray.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface TokenRepository extends JpaRepository<Token, BigInteger> {
    Token findByToken(String token);
}
