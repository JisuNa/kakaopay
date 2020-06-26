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
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public BigInteger seq;
    @Column(name="token")
    public String token;
    @Column(name="created_at")
    public String createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = StringUtils.isEmpty(this.createdAt) ? DateUtil.getNow() : this.createdAt;
    }

}
