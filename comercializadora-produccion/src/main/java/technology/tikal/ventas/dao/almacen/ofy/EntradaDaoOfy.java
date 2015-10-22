package technology.tikal.ventas.dao.almacen.ofy;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;

import technology.tikal.ventas.dao.almacen.EntradaDao;
import technology.tikal.ventas.model.almacen.ofy.EntradaOfy;
import technology.tikal.ventas.model.pedido.Pedido;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class EntradaDaoOfy extends RegistroAlmacenDaoOfy<EntradaOfy> implements EntradaDao {

    @Override
    public Query<EntradaOfy> buildQuery() {
        Query<EntradaOfy> query = ofy().load().type(EntradaOfy.class);
        return query;
    }

    @Override
    public Key<EntradaOfy> buildKey(Pedido parent, Long id) {
        return Key.create(Key.create(parent), EntradaOfy.class, id);
    }

}
