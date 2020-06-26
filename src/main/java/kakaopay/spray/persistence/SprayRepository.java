package kakaopay.spray.persistence;

import kakaopay.spray.entity.Spray;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;

public interface SprayRepository extends JpaRepository<Spray, BigInteger> {

    Spray findByTokenSeq(BigInteger tokenSeq);

    @Query("SELECT seq FROM Spray WHERE tokenSeq = :tokenSeq and userSeq = :userSeq")
    BigInteger findByTokenSeqAndUserSeq(BigInteger tokenSeq, BigInteger userSeq);

    @Query("SELECT userSeq FROM Spray WHERE tokenSeq = :tokenSeq")
    BigInteger findUserSeqByTokenSeq(BigInteger tokenSeq);

}
