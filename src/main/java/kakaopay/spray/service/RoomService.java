package kakaopay.spray.service;

import kakaopay.spray.dto.ResponseDTO;
import kakaopay.spray.dto.SprayDTO;
import kakaopay.spray.entity.Room;

import java.math.BigInteger;
import java.util.List;

public interface RoomService {
    ResponseDTO valid(SprayDTO sprayDTO);
    List<Room> getRooms(String roomId);
}
