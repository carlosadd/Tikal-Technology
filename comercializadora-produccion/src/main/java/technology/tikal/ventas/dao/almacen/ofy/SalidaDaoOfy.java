package technology.tikal.ventas.dao.almacen.ofy;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;

import technology.tikal.ventas.dao.almacen.SalidaDao;
import technology.tikal.ventas.model.almacen.ofy.SalidaOfy;
import technology.tikal.ventas.model.pedido.Pedido;
import static com.googlecode.objectify.ObjectifyService.ofy;

public class SalidaDaoOfy extends RegistroAlmacenDaoOfy<SalidaOfy> implements SalidaDao {

    @Override
    public Query<SalidaOfy> buildQuery() {
        Query<SalidaOfy> query = ofy().load().type(SalidaOfy.class);
        return query;
    }

    @Override
    public Key<SalidaOfy> buildKey(Pedido parent, Long id) {
        return Key.create(Key.create(parent), SalidaOfy.class, id);
    }

}
