package kakaopay.spray.persistence;

import kakaopay.spray.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, BigInteger> {

    List<Room> findAllByRoomId(String roomId);
}
