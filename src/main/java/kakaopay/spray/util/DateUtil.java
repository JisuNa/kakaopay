package kakaopay.spray.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String getNow() {
        Date date_now = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date_now);
    }

}
