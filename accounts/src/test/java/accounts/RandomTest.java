package accounts;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;

public class RandomTest {

    public static void main(String[] args) {
        SecureRandom random = new SecureRandom();
        for(int i = 0; i < 30; i++) {
            String r = new BigInteger(130, random).toString(32);
            System.out.println(r);
        }
        /*for(int i = 0; i < 30; i++) {
            Date ahora = new Date();
            String r =Long.toString(ahora.getTime(), 32);
            try {
                Thread.currentThread().sleep(1);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            r = r + new BigInteger(85, random).toString(32);
            System.out.println(r);
        }*/
    }

}
