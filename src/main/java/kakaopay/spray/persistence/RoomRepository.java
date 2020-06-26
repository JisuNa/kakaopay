package kakaopay.spray.persistence;

import kakaopay.spray.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, BigInteger> {

    List<Room> findAllByRoomId(String roomId);

    @Query("SELECT seq FROM Room WHERE roomId = :roomId AND userSeq = :userSeq")
    BigInteger findByRoomIdAndUserSeq(String roomId, BigInteger userSeq);
}
