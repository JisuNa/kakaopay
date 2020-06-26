package kakaopay.spray.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;

@Data
@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public BigInteger seq;
    @Column(name="room_id")
    public String roomId;
    @Column(name="user_seq")
    public BigInteger userSeq;
}
