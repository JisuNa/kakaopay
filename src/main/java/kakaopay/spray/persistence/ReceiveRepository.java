package kakaopay.spray.persistence;

import kakaopay.spray.entity.Receive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface ReceiveRepository extends JpaRepository<Receive, BigInteger> {

    List<Receive> findByTokenSeq(BigInteger tokenSeq);

    @Query("SELECT seq FROM Receive WHERE tokenSeq=:tokenSeq AND userSeq=:userSeq")
    BigInteger findByTokenSeqAndUserSeq(BigInteger tokenSeq, BigInteger userSeq);

    @Query("SELECT r FROM Receive r WHERE r.tokenSeq=:tokenSeq AND r.userSeq is not null")
    List<Receive> findByTokenSeqAndNotNullUserSeq(BigInteger tokenSeq);

    @Query("SELECT r FROM Receive r WHERE r.tokenSeq=:tokenSeq AND r.userSeq is null")
    List<Receive> findByTokenSeqAndNullUserSeq(BigInteger tokenSeq);
}
