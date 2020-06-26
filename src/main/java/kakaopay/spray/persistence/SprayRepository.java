package kakaopay.spray.persistence;

import kakaopay.spray.entity.Spray;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface SprayRepository extends JpaRepository<Spray, BigInteger> {
}
