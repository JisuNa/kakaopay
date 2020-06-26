package kakaopay.spray.controller;

import kakaopay.spray.dto.ResponseDTO;
import kakaopay.spray.dto.SprayDTO;
import kakaopay.spray.service.UserService;
import kakaopay.spray.service.ReceiveService;
import kakaopay.spray.service.RoomService;
import kakaopay.spray.service.SprayService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;


@RestController
@RequestMapping(value="/spray")
public class SprayController {

    private UserService accountService;
    private SprayService sprayService;
    private ReceiveService receiveService;
    private RoomService roomService;

    public SprayController(UserService accountService, SprayService sprayService, ReceiveService receiveService, RoomService roomService) {
        this.accountService = accountService;
        this.sprayService = sprayService;
        this.receiveService = receiveService;
        this.roomService = roomService;
    }

    /* 뿌리기 */
    @PostMapping(value="")
    public ResponseEntity<?> spray(@RequestBody SprayDTO sprayDTO, @RequestHeader(value="X-USER-ID") BigInteger userId, @RequestHeader(value="X-ROOM-ID") String roomId) {

        sprayDTO.setUserId(userId);
        sprayDTO.setRoomId(roomId);

        return new ResponseEntity<>(sprayService.setSpray(sprayDTO), HttpStatus.OK);
    }

    /* 받기 */
    @PostMapping(value="/receive")
    public ResponseEntity<?> receive() {

        return new ResponseEntity<>("receive", HttpStatus.OK);
    }

    /* 조회 */
    @GetMapping(value="")
    public ResponseEntity<?> check() {

        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
