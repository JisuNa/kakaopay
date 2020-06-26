package kakaopay.spray.dto;

import lombok.Data;

import java.util.List;

@Data
public class SprayInfoDTO {
    private int amount;
    private String status;
    private String createdAt;
    private int totalReceived;
    private List<ReceiveDTO> receivedList;

    public SprayInfoDTO(int amount, String status, String createdAt) {
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }
}
