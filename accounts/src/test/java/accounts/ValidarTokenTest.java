package accounts;

import com.warrenstrange.googleauth.GoogleAuthenticator;

public class ValidarTokenTest {

    public static void main(String[] args) {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        System.out.println(gAuth.authorize("QQXSFPY5C6NVDYQA", 193174));
    }

}
