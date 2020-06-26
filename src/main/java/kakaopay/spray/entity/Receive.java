package kakaopay.spray.entity;

import kakaopay.spray.util.DateUtil;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

@Data
@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Receive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public BigInteger seq;
    @Column(name="token_seq")
    public BigInteger tokenSeq;
    @Column(name="user_seq")
    public BigInteger userSeq;
    @Column(name="amount")
    public int amount;
    @Column(name="created_at")
    public String createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = StringUtils.isEmpty(this.createdAt) ? DateUtil.getNow() : this.createdAt;
    }
}
