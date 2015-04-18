/**
 *   Copyright 2015 Tikal-Technology
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */
package technology.tikal.gae.dao.template;

import java.util.List;

import technology.tikal.gae.pagination.model.PaginationData;


/**
 * @author Nekorp
 * Template para los daos.
 * @param <T> el tipo de entidad que maneja el dao.
 * @param <K> el tipo de la llave de la entidad que maneja el dao.
 * @param <P> el tipo del padre.
 * @param <F> el tipo del filtro.
 * @param <PD> el tipo de la pagina
 */
public interface ChildEntityDAO<T,K,P,F extends FiltroBusqueda, PD extends PaginationData<?>> {

    List<T> consultarTodos(P parent, F filtro, PD pagination);
    
    void guardar(P parent, T objeto);
    
    T consultar(P parent, K id, Class<?>... group);

    void borrar(P parent, T objeto);
}
