package json;

import java.util.ArrayList;
import java.util.List;

import technology.tikal.gae.security.model.InternalGrantedAuthority;
import technology.tikal.gae.system.security.model.UserSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonMapperTest {

    public static void main(String arg[]) {
        ObjectMapper mapper = new ObjectMapper();
        UserSession session = new UserSession("usuario1", "usuario1");
        InternalGrantedAuthority authority = new InternalGrantedAuthority("ROLE_USER");
        //TODO tener un mecanismo para configurar los permisos de cada usuario por sistema
        List<InternalGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);
        session.setAuthorities(authorities);
        try {
            System.out.println(mapper.writeValueAsString(session));
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
