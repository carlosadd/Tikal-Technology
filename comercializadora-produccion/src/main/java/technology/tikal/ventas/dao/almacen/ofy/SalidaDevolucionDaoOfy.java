package technology.tikal.ventas.dao.almacen.ofy;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;

import technology.tikal.ventas.dao.almacen.SalidaDevolucionDao;
import technology.tikal.ventas.model.almacen.ofy.SalidaDevolucionOfy;
import technology.tikal.ventas.model.pedido.Pedido;
import static com.googlecode.objectify.ObjectifyService.ofy;

public class SalidaDevolucionDaoOfy extends RegistroAlmacenDaoOfy<SalidaDevolucionOfy> implements SalidaDevolucionDao {

    @Override
    public Query<SalidaDevolucionOfy> buildQuery() {
        Query<SalidaDevolucionOfy> query = ofy().load().type(SalidaDevolucionOfy.class);
        return query;
    }

    @Override
    public Key<SalidaDevolucionOfy> buildKey(Pedido parent, Long id) {
        return Key.create(Key.create(parent), SalidaDevolucionOfy.class, id);
    }

}
