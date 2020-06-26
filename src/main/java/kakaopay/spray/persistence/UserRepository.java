package kakaopay.spray.persistence;

import kakaopay.spray.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;

public interface UserRepository extends JpaRepository<User, BigInteger> {

    @Query("SELECT seq FROM User WHERE userId = :userId")
    BigInteger findSeqByUserId(BigInteger userId);

    @Query("SELECT userId FROM User WHERE seq = :seq")
    BigInteger findUserIdBySeq(BigInteger seq);
}
