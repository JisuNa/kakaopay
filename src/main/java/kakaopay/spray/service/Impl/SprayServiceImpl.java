package kakaopay.spray.service.Impl;

import kakaopay.spray.dto.ReceiveDTO;
import kakaopay.spray.dto.Response;
import kakaopay.spray.dto.SprayDTO;
import kakaopay.spray.dto.SprayInfoDTO;
import kakaopay.spray.entity.Receive;
import kakaopay.spray.entity.Room;
import kakaopay.spray.entity.Spray;
import kakaopay.spray.entity.Token;
import kakaopay.spray.persistence.TokenRepository;
import kakaopay.spray.persistence.UserRepository;
import kakaopay.spray.persistence.SprayRepository;
import kakaopay.spray.service.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

@Service
public class SprayServiceImpl implements SprayService {

    private UserService userService;
    private RoomService roomService;
    private ReceiveService receiveService;
    private TokenService tokenService;
    private UserRepository userRepository;
    private SprayRepository sprayRepository;
    private TokenRepository tokenRepository;

    public SprayServiceImpl(SprayRepository sprayRepository, UserRepository userRepository, UserService userService, RoomServiceImpl roomService, ReceiveService receiveService, TokenService tokenService, TokenRepository tokenRepository) {
        this.userService = userService;
        this.sprayRepository = sprayRepository;
        this.userRepository = userRepository;
        this.roomService = roomService;
        this.receiveService = receiveService;
        this.tokenService = tokenService;
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    @Override
    public Response setSpray(SprayDTO sprayDTO) {

        Response res = new Response();
        kakaopay.spray.entity.Spray spray = new kakaopay.spray.entity.Spray();

        try {
            BigInteger userSeq = userService.getSeq(sprayDTO.getUserId());
            if(ObjectUtils.isEmpty(userSeq)) throw new Exception("존재하지 않은 유저ID입니다.");

            List<Room> roomMember = roomService.getRooms(sprayDTO.getRoomId());

            int roomMemberSize = roomMember.size();
            if(roomMemberSize < 1) throw new Exception("존재하지 않은 Room ID 입니다.");

            boolean isJoinedRoom = false;
            for(Room room : roomMember) {
                isJoinedRoom = userSeq.equals(room.getUserSeq());
                if(isJoinedRoom) break;
            }
            if(!isJoinedRoom) throw new Exception("요청자는 해당방에 있지 않습니다.");

            if(roomMemberSize < sprayDTO.getNumberOfRecipients()) throw new Exception("뿌리기 받는 인원 수는 방 인원 수보다 많을 수 없습니다.");

            Token token = tokenService.generateToken();
            if(StringUtils.isEmpty(token.getToken())) throw new Exception("토큰 생성에 문제가 발생했습니다.");

            spray.setRoomSeq(roomMember.get(0).getSeq());
            spray.setUserSeq(userSeq);
            spray.setAmount(sprayDTO.getAmount());
            spray.setTokenSeq(token.getSeq());
            spray.setNumberOfRecipients(sprayDTO.getNumberOfRecipients());
            Spray sprayInfo = sprayRepository.save(spray);

            sprayDTO.setToken(token.getToken());
            sprayDTO.setCreatedAt(sprayInfo.getCreatedAt());
            sprayDTO.setStatus(sprayInfo.getStatus());

            Receive receive = receiveService.initReceive(token.getSeq(), userSeq, sprayDTO.getAmount(), sprayDTO.getNumberOfRecipients());
            if(ObjectUtils.isEmpty(receive)) throw new Exception("뿌리기 분배중 오류가 발생하였습니다.");

            res.setCode(200);
            res.setData(sprayDTO);
        } catch(Exception e) {
            res.setCode(400);
            res.setMsg(e.getMessage());
        }

        return res;
    }

    @Override
    public Response getSprayAndReceive(String requestToken, BigInteger userId) {

        Response res = new Response();

        try {
            Token token = tokenRepository.findByToken(requestToken);
            if(ObjectUtils.isEmpty(token)) throw new Exception("유효하지 않은 토큰입니다.");

            // 토큰 만료 체크
            if(tokenService.checkExpired(token.getCreatedAt(), 10080)) throw new Exception("해당 토큰은 만료되었습니다.");

            BigInteger tokenSeq = token.getSeq();
            BigInteger userSeq = userRepository.findSeqByUserId(userId);

            BigInteger sprayUserSeq = sprayRepository.findUserSeqByTokenSeq(tokenSeq);
            if(!userSeq.equals(sprayUserSeq)) throw new Exception("뿌리기 등록자만 조회가 가능합니다.");

            //현재상태, 뿌린시각, 뿌린금액, 받기완료된 금액, 받기완료된 정보
            List<ReceiveDTO> receiveList = receiveService.getReceives(tokenSeq);
            SprayInfoDTO sprayInfoDTO = getSpray(tokenSeq);
            sprayInfoDTO.setReceivedList(receiveList);

            int totalReceived = 0;
            for(ReceiveDTO receiveDTO : receiveList) {
                totalReceived += receiveDTO.getAmount();
            }
            sprayInfoDTO.setTotalReceived(totalReceived);
            res.setCode(200);
            res.setData(sprayInfoDTO);
        } catch(Exception e) {
            res.setCode(400);
            res.setMsg(e.getMessage());
        }
        return res;
    }

    public SprayInfoDTO getSpray(BigInteger tokenSeq) {
        Spray spray = sprayRepository.findByTokenSeq(tokenSeq);
        return new SprayInfoDTO(spray.getAmount(), spray.getStatus(), spray.getCreatedAt());
    }
}
