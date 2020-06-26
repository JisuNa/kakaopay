package kakaopay.spray.service;

import kakaopay.spray.entity.Receive;

import java.math.BigInteger;

public interface ReceiveService {
    Receive initReceive(BigInteger tokenSeq, BigInteger userSeq, int amount, int numberOfRecipients);
}
