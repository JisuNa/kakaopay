package kakaopay.spray.service.Impl;

import kakaopay.spray.dto.SprayDTO;
import kakaopay.spray.entity.Receive;
import kakaopay.spray.persistence.ReceiveRepository;
import kakaopay.spray.service.ReceiveService;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class ReceiveServiceImpl implements ReceiveService {

    private ReceiveRepository receiveRepository;

    public ReceiveServiceImpl(ReceiveRepository receiveRepository) {
        this.receiveRepository = receiveRepository;
    }

    @Override
    public Receive initReceive(BigInteger tokenSeq, BigInteger userSeq, int amount, int numberOfRecipients) {

        int total_receive = 0;
        Receive receive = null;

        try {
            for(int i=0; i<numberOfRecipients; i++) {
                receive = new Receive();

                int splitAmount = 0;

                if(i != numberOfRecipients-1) {
                    splitAmount = random_receive(amount-total_receive);
                    total_receive += splitAmount;
                } else {
                    splitAmount = amount - total_receive;
                }
                receive.setTokenSeq(tokenSeq);
                receive.setAmount(splitAmount);
                receive = receiveRepository.save(receive);
            }
        } catch(Exception e) {
            receive = new Receive();
        }
        return receive;
    }

    private int random_receive(int remain_amount) {
        return (int)(Math.random() * remain_amount)+1;
    }
}
