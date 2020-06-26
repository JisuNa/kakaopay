package kakaopay.spray.service.Impl;

import kakaopay.spray.dto.Response;
import kakaopay.spray.dto.SprayDTO;
import kakaopay.spray.entity.Room;
import kakaopay.spray.persistence.RoomRepository;
import kakaopay.spray.service.RoomService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private RoomRepository roomRepository;

    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Response valid(SprayDTO sprayDTO) {

        Response res = new Response();

        try {
            List<Room> rooms = roomRepository.findAllByRoomId(sprayDTO.getRoomId());

            if(0 == rooms.size()) throw new Exception("방이 없습니다.");
            if(sprayDTO.getNumberOfRecipients() > rooms.size()) throw new Exception("방 인원수보다 많을 수 없습니다.");

            res.setCode(200);
        } catch(Exception e) {
            res.setCode(400);
            res.setMsg(e.getMessage());
        }
        return res;
    }

    @Override
    public List<Room> getRooms(String roomId) {
        return roomRepository.findAllByRoomId(roomId);
    }


}
