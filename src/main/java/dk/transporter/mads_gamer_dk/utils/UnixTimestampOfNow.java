package dk.transporter.mads_gamer_dk.utils;

import java.util.Date;

public class UnixTimestampOfNow {

    public static Integer getTime(){

        try {
            Date now = new Date();
            Integer ut3 = Math.toIntExact(now.getTime() / 1000L);
            return ut3;
        }catch(ArithmeticException e){
            System.out.println(e);
            return 0;
        }catch(Exception e){
            System.out.println(e);
            return 0;
        }


    }
}
