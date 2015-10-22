package technology.tikal.ventas.dao.almacen.ofy;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;

import technology.tikal.ventas.dao.almacen.EntradaDevolucionDao;
import technology.tikal.ventas.model.almacen.ofy.EntradaDevolucionOfy;
import technology.tikal.ventas.model.pedido.Pedido;
import static com.googlecode.objectify.ObjectifyService.ofy;

public class EntradaDevolucionDaoOfy extends RegistroAlmacenDaoOfy<EntradaDevolucionOfy> implements EntradaDevolucionDao {

    @Override
    public Query<EntradaDevolucionOfy> buildQuery() {
        Query<EntradaDevolucionOfy> query = ofy().load().type(EntradaDevolucionOfy.class);
        return query;
    }

    @Override
    public Key<EntradaDevolucionOfy> buildKey(Pedido parent, Long id) {
        return Key.create(Key.create(parent), EntradaDevolucionOfy.class, id);
    }

}
