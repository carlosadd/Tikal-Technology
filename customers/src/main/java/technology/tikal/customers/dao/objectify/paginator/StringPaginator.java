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
package technology.tikal.customers.dao.objectify.paginator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import technology.tikal.string.util.StringNormalizer;

import com.googlecode.objectify.cmd.Query;

/**
 * @author Nekorp
 * @param <T> El tipo de objeto del dao
 * @param <K> El tipo del parent si aplica
 */
public abstract class StringPaginator<T,K> {

    /**
     * numeros magicos, esto va a explotar si existen
     * maxRetry*stepDuplicated
     * registros repetidos ( mismo string )
     */
    private static final int DEFAULT_STEP = 3;
    private static final int DEFAULT_MAX_RETRY = 10;
    /**
     * el incremento en el numero de duplicados en cada retry.
     */
    private int stepDuplicated = StringPaginator.DEFAULT_STEP;
    /**
     * cuantas veces lo intentara
     */
    private int maxRetry = StringPaginator.DEFAULT_MAX_RETRY;
    
    public List<T> consultarTodosTemplate(PaginationContext<K> context) {
        if ((context.getPagination().hasSince() && !context.getPagination().hasSinceId()) 
                || (!context.getPagination().hasSince() && context.getPagination().hasSinceId())) {
            throw new IllegalArgumentException("Para paginar sobre una propiedad que puede tener repetidos se ocupan los dos id");
        }
        if (StringUtils.isNotEmpty(getStringFilter(context))){
            return consultarTodosConFiltro(context, stepDuplicated, 1);
        } else {
            return consultarTodosSinFiltro(context, stepDuplicated, 1);
        }
    }
    
    public abstract Query<T> buildQuery(PaginationContext<K> context);
    
    public abstract Long getId(T pojo, PaginationContext<K> context);
    
    public abstract String getIndexValue(T pojo, PaginationContext<K> context);
    
    public abstract String getStringFilter(PaginationContext<K> context);
    
    private List<T> consultarTodosSinFiltro(PaginationContext<K> context, int duplicated, int retry) {
        List<T> result;
        Query<T> query = buildQuery(context);
        if (context.getPagination().hasSince()) {
            String since = StringNormalizer.normalize(context.getPagination().getSince());
            query = query.filter(context.getIndexOfy() + " >=", since);
            result = resolvePaginationSince(query, context, duplicated);
            if (result == null) {
                if (retry > this.maxRetry) {
                    throw new InternalError("No se logro paginar");
                }
                //se incrementa el numero de duplicados y se busca de nuevo
                result = this.consultarTodosSinFiltro(context, duplicated + stepDuplicated, retry + 1);
            }
        } else {
            result = resolvePagination(query, context);
        }
        return result;
    }
    
    private List<T> consultarTodosConFiltro(PaginationContext<K> context, int duplicated, int retry) {
        List<T> result;
        Query<T> query = buildQuery(context);
        if (context.getPagination().hasSince()) {
            String since = StringNormalizer.normalize(context.getPagination().getSince());
            query = query.filter(context.getIndexOfy() + " >=", since)
                    .filter(context.getIndexOfy() + " < ", getStringFilter(context) + "\uFFFD");
            result = resolvePaginationSince(query, context, duplicated);
            if (result == null) {
                if (retry > this.maxRetry) {
                    throw new InternalError("No se logro paginar");
                }
                //se incrementa el numero de duplicados y se busca de nuevo
                result = this.consultarTodosConFiltro(context, duplicated + stepDuplicated, retry + 1);
            }
        } else {
            query = query.filter(context.getIndexOfy() + " >=", getStringFilter(context))
                    .filter(context.getIndexOfy() + " < ", getStringFilter(context) + "\uFFFD");
            result = resolvePagination(query, context);
        }
        return result;
    }
    
    private List<T> resolvePaginationSince(Query<T> query, PaginationContext<K> context, int duplicated) {
        List<T> result;
        boolean limited = context.getPagination().getMaxResults() > 0;
        if (limited) {
            query = query.limit(context.getPagination().getMaxResults() + 1 + duplicated);
        }
        result = query.list();
        List<T> newResult = new ArrayList<>();
        boolean sinceFound = false;
        for (T x : result) {
            if (!sinceFound && getId(x, context).equals(context.getPagination().getSinceId())) {
                sinceFound = true;
            }
            if (sinceFound) {
                newResult.add(x);
            }
        }
        int discarded = result.size() - newResult.size();
        if (discarded > duplicated) {
            //se descartaron mas de los esperados, se debe reiniciar la busqueda
            return null;
        }
        if (limited && newResult.size() > context.getPagination().getMaxResults()) {
            //si alcanzan para hacer mas paginas, al menos en uno
            while(newResult.size() > context.getPagination().getMaxResults() + 1) {
                newResult.remove(newResult.size() - 1);
            }
            T ultimo = newResult.get(context.getPagination().getMaxResults());
            context.getPagination().setNextId(getId(ultimo, context));
            context.getPagination().setNext(getIndexValue(ultimo, context));
            newResult.remove(context.getPagination().getMaxResults());
            return newResult;
        }
        return newResult;
    }
    
    private List<T> resolvePagination(Query<T> query, PaginationContext<K> context) {
        List<T> result;
        boolean limited = context.getPagination().getMaxResults() > 0;
        if (limited) {
            query = query.limit(context.getPagination().getMaxResults() + 1);
        }
        result = query.list();
        if (limited && result.size() > context.getPagination().getMaxResults()) {
            //si alcanzan para hacer mas paginas, al menos en uno
            T ultimo = result.get(context.getPagination().getMaxResults());
            context.getPagination().setNextId(getId(ultimo, context));
            context.getPagination().setNext(getIndexValue(ultimo, context));
            result.remove(context.getPagination().getMaxResults());
        }
        return result;
    }
}
