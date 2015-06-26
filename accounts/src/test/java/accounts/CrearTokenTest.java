package accounts;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

public class CrearTokenTest {

    public static void main(String arg[]) {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        final GoogleAuthenticatorKey key = gAuth.createCredentials();
        System.out.println(key.getKey());
        String url = GoogleAuthenticatorQRGenerator.getOtpAuthURL("tikal", "test", key);
        System.out.println(url);
    }
}
