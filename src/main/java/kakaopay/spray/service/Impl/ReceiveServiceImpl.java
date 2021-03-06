package kakaopay.spray.service.Impl;

import kakaopay.spray.dto.ReceiveDTO;
import kakaopay.spray.dto.Response;
import kakaopay.spray.entity.Receive;
import kakaopay.spray.entity.Spray;
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

        //토큰이 10분 지났는지 --
        //자신이 뿌리기 한건지 --
        //뿌린사람의 방에 있는사람인지 --
        //이미 받은 내역이 있는지 --
        //받을 수 있는 뿌리기가 있는지 --

        try {
            Token token = tokenRepository.findByToken(requestToken);
            if(ObjectUtils.isEmpty(token)) throw new Exception("잘못된 토큰입니다.");

            BigInteger tokenSeq = token.getSeq();
            String tokenCreatedAt = token.getCreatedAt();
            if(tokenService.checkExpired(tokenCreatedAt, 10)) throw new Exception("해당 토큰은 만료되었습니다.");

            BigInteger userSeq = userRepository.findSeqByUserId(userId);
            Spray spray = sprayRepository.findByTokenSeq(tokenSeq);
            if(ObjectUtils.isEmpty(spray)) throw new Exception("토큰에 해당하는 뿌리기가 없습니다.");
            if("COMPLETE".equals(spray.getStatus())) throw new Exception("뿌리기가 완료됐습니다.");
            if(userSeq.equals(spray.getUserSeq())) throw new Exception("자신이 뿌리기한 건은 받을 수 없습니다.");

            BigInteger roomSeq = roomRepository.findByRoomIdAndUserSeq(roomId, userSeq);
            if(ObjectUtils.isEmpty(roomSeq)) throw new Exception("뿌리기한 사람과 같은방에 있어야 합니다.");
            
            // 받을수 있는 뿌리기 확인
            List<Receive> receiveList = receiveRepository.findByTokenSeq(tokenSeq);
            if(ObjectUtils.isEmpty(receiveList)) throw new Exception("받을 수 있는 뿌리기가 없습니다.");

            for(Receive receive : receiveList) {
                if(userSeq.equals(receive.getUserSeq())) throw new Exception("이미 받은 내역이 존재합니다.");
            }

            Receive receive = receiveList.get(0);
            receive.setUserSeq(userSeq);
            receive = receiveRepository.save(receive);

            // 마지막 남은 받기일 때
            if(receiveList.size() == 1) {
                spray.setStatus("COMPLETE");
                sprayRepository.save(spray);
            }

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
