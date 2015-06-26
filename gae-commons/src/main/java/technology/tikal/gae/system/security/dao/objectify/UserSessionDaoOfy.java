package technology.tikal.gae.system.security.dao.objectify;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;
import com.googlecode.objectify.cmd.Query;

import technology.tikal.gae.dao.template.FiltroBusqueda;
import technology.tikal.gae.pagination.model.PaginationData;
import technology.tikal.gae.system.security.dao.UserSessionDao;
import technology.tikal.gae.system.security.model.UserSession;

public class UserSessionDaoOfy implements UserSessionDao {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            if (!StringUtils.isEmpty(username)) {
                return consultar(username);
            } else {
                throw new UsernameNotFoundException("UserNotFound");
            }
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException("UserNotFound");
        }
    }

    @Override
    public List<UserSession> consultarTodos(FiltroBusqueda filtro, PaginationData<String> pagination) {
        List<UserSession> result;
        Query<UserSession> query = ofy().load().type(UserSession.class);
        if (StringUtils.isNotEmpty(pagination.getSinceId())) {
            query = query.filterKey(">=", Key.create(UserSession.class, pagination.getSinceId()));
        }
        if (pagination.getMaxResults() > 0) {
            query = query.limit(pagination.getMaxResults() + 1);
        }
        result = query.list();
        if (pagination.getMaxResults() != 0 && result.size() > pagination.getMaxResults()) {
            UserSession ultimo = result.get(pagination.getMaxResults());
            pagination.setNextId(ultimo.getUsername());
            result.remove(pagination.getMaxResults());
        }
        return result;
    }

    @Override
    public void guardar(UserSession objeto) {
        ofy().save().entity(objeto).now();
    }

    @Override
    public UserSession consultar(String id, Class<?>... group) {
        return ofy().load().key(Key.create(UserSession.class, id)).safe();
    }

    @Override
    public void borrar(UserSession objeto) {
        ofy().delete().entities(objeto).now();
    }

}
