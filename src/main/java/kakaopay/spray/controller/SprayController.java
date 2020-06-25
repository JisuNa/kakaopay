package kakaopay.spray.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.reflect.annotation.ExceptionProxy;


@RestController
@RequestMapping(value="/spray")
public class SprayController {

    /* 뿌리기 */
    @PostMapping(value="")
    public ResponseEntity<?> spray(@RequestHeader(value = "X-USER-ID") String userId) {

        return new ResponseEntity<>(userId, HttpStatus.OK);

    }

    /* 받기 */
    @PutMapping(value="/test")
    public ResponseEntity<?> receive() {

        return new ResponseEntity<>("receive", HttpStatus.OK);
    }

    /* 조회 */
    @GetMapping(value="/test")
    public ResponseEntity<?> check() {

        return new ResponseEntity<>("check", HttpStatus.OK);
    }
}
