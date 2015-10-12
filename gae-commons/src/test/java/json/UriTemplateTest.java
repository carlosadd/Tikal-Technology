package json;

import java.text.MessageFormat;



public class UriTemplateTest {

    public static void main(String arg[]) {
        
        String data = "/api/parent/{0,number,#}/coso";
        //System.out.println(mf.format(data, "\\p{Alnum}*"));
        System.out.println(MessageFormat.format(data, 1234567894123456l));
        /*while(data.contains("{")) {
            data = MessageFormat.format(data, "\\p{Alnum}*");
        }
        System.out.println(data);*/
    }
}
