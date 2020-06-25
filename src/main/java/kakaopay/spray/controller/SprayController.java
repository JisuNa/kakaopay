package kakaopay.spray.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SprayController {

    @GetMapping(value="/index")
    public ResponseEntity<?> index() {

        return new ResponseEntity<>("good", HttpStatus.OK);
    }
}
