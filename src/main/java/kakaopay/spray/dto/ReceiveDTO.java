package kakaopay.spray.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class ReceiveDTO {
    private BigInteger userId;
    private int amount;

    public ReceiveDTO(BigInteger userId, int amount) {
        this.userId = userId;
        this.amount = amount;
    }
}
