package kakaopay.spray.service;

import kakaopay.spray.dto.ReceiveDTO;
import kakaopay.spray.dto.Response;
import kakaopay.spray.entity.Receive;

import java.math.BigInteger;
import java.util.List;

public interface ReceiveService {
    Receive initReceive(BigInteger tokenSeq, BigInteger userSeq, int amount, int numberOfRecipients);
    Response receive(String token, BigInteger userId, String roomId);
    List<ReceiveDTO> getReceives(BigInteger tokenSeq);
}
