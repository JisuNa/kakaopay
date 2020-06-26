package kakaopay.spray.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class SprayDTO {
    private BigInteger userId;
    private String roomId;
    private String token;
    private int amount;
    private int numberOfRecipients;
}
