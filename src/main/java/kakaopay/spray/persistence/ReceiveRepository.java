package kakaopay.spray.persistence;

import kakaopay.spray.entity.Receive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;

public interface ReceiveRepository extends JpaRepository<Receive, BigInteger> {
}
