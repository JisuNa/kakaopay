package kakaopay.spray.entity;

import kakaopay.spray.util.DateUtil;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.math.BigInteger;

@Data
@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Spray {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public BigInteger seq;
    @Column(name="room_seq")
    public BigInteger roomSeq;
    @Column(name="token_seq")
    public BigInteger tokenSeq;
    @Column(name="status")
    public String status;
    @Column(name="user_seq")
    public BigInteger userSeq;
    @Column(name="number_of_recipients")
    public int numberOfRecipients;
    @Column(name="amount")
    public int amount;
    @Column(name="sprayed_amount")
    public int sprayedAmount;
    @Column(name="created_at")
    public String createdAt;
    @Column(name="finished_at")
    public String finishedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = StringUtils.isEmpty(this.createdAt) ? DateUtil.getNow() : this.createdAt;
        this.status = StringUtils.isEmpty(this.status) ? "PROCESSING" : this.status;
    }
}
