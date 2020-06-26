package kakaopay.spray.service.Impl;

import kakaopay.spray.dto.ReceiveDTO;
import kakaopay.spray.dto.Response;
import kakaopay.spray.entity.Receive;
import kakaopay.spray.entity.Token;
import kakaopay.spray.persistence.*;
import kakaopay.spray.service.ReceiveService;
import kakaopay.spray.service.TokenService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReceiveServiceImpl implements ReceiveService {

    private TokenService tokenService;
    private ReceiveRepository receiveRepository;
    private TokenRepository tokenRepository;
    private SprayRepository sprayRepository;
    private UserRepository userRepository;
    private RoomRepository roomRepository;

    public ReceiveServiceImpl(ReceiveRepository receiveRepository, TokenRepository tokenRepository, SprayRepository sprayRepository, UserRepository userRepository, RoomRepository roomRepository, TokenService tokenService) {
        this.tokenService = tokenService;
        this.receiveRepository = receiveRepository;
        this.tokenRepository = tokenRepository;
        this.sprayRepository = sprayRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
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

    @Override
    public Response receive(String requestToken, BigInteger userId, String roomId) {

        Response res = new Response();

        //토큰이 10분 지났는 --
        //자신이 뿌리기 한건지 --
        //뿌린사람의 방에 있는사람인지 --
        //이미 받은 내역이 있는지 --
        //받을 수 있는 뿌리기가 있는지 --

        try {
            Token token = tokenRepository.findByToken(requestToken);

            BigInteger tokenSeq = token.getSeq();
            String tokenCreatedAt = token.getCreatedAt();
            if(tokenService.checkExpired(tokenCreatedAt, 10)) throw new Exception("해당 토큰은 만료되었습니다.");

            BigInteger userSeq = userRepository.findSeqByUserId(userId);
            BigInteger spraySeq = sprayRepository.findByTokenSeqAndUserSeq(tokenSeq, userSeq);
            if(ObjectUtils.isNotEmpty(spraySeq)) throw new Exception("자신이 뿌리기한 건은 받을 수 없습니다.");

            BigInteger roomSeq = roomRepository.findByRoomIdAndUserSeq(roomId, userSeq);
            if(ObjectUtils.isEmpty(roomSeq)) throw new Exception("올바르지 않은 접근입니다.");

            //받은내역은 tokenSeq, userSeq로 조회해서 있으면
            BigInteger receiveSeq = receiveRepository.findByTokenSeqAndUserSeq(tokenSeq, userSeq);
            if(ObjectUtils.isNotEmpty(receiveSeq)) throw new Exception("이미 받은 내역이 존재합니다.");

            // 받을수 있는 뿌리기 확인
            Receive receive = receiveRepository.findByTokenSeq(tokenSeq).get(0);
            if(ObjectUtils.isEmpty(receive)) throw new Exception("받을 수 있는 뿌리기가 없습니다.");

            receive.setUserSeq(userSeq);
            receive = receiveRepository.save(receive);

            ReceiveDTO receiveDTO = new ReceiveDTO(userId, receive.getAmount());

            res.setCode(200);
            res.setData(receiveDTO);
        } catch(Exception e) {
            res.setCode(400);
            res.setMsg(e.getMessage());
        }
        return res;
    }

    @Override
    public List<ReceiveDTO> getReceives(BigInteger tokenSeq) {

        List<ReceiveDTO> receiveDTOList = new ArrayList<>();
        List<Receive> receiveList = receiveRepository.findByTokenSeqAndNotNullUserSeq(tokenSeq);

        for(Receive receive : receiveList) {
            BigInteger userId = userRepository.findUserIdBySeq(receive.getUserSeq());
            ReceiveDTO receiveDTO = new ReceiveDTO(userId, receive.getAmount());
            receiveDTOList.add(receiveDTO);
        }

        return receiveDTOList;
    }

    private int random_receive(int remain_amount) {
        return (int)(Math.random() * remain_amount)+1;
    }
}
