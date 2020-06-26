package kakaopay.spray.service;

import kakaopay.spray.dto.Response;
import kakaopay.spray.dto.SprayDTO;

public interface SprayService {
    Response setSpray(SprayDTO sprayDTO);
}
