package kakaopay.spray.service;

import kakaopay.spray.dto.Response;
import kakaopay.spray.dto.SprayDTO;
import kakaopay.spray.entity.Room;

import java.util.List;

public interface RoomService {
    Response valid(SprayDTO sprayDTO);
    List<Room> getRooms(String roomId);
}
