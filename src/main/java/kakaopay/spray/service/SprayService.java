package kakaopay.spray.service;

import kakaopay.spray.dto.ResponseDTO;
import kakaopay.spray.dto.SprayDTO;

public interface SprayService {
    ResponseDTO setSpray(SprayDTO sprayDTO);
}
