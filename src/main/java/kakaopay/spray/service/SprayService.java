package kakaopay.spray.service;

import kakaopay.spray.dto.Response;
import kakaopay.spray.dto.SprayDTO;

import java.math.BigInteger;

public interface SprayService {
    Response setSpray(SprayDTO sprayDTO);
    Response getSprayAndReceive(String token, BigInteger userId);
}
