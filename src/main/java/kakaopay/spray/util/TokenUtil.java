package kakaopay.spray.util;

import org.apache.commons.lang3.RandomStringUtils;

public class TokenUtil {

    public static String getSprayToken() {
        return generateToken()+"-"+generateToken()+"-"+generateToken();
    }

    private static String generateToken() {
        return RandomStringUtils.randomAlphanumeric(5);
    }
}
