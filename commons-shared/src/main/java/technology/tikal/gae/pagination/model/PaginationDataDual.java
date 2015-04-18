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
package technology.tikal.gae.pagination.model;

/**
 * @author Nekorp
 * En caso de que se quiera paginar usando un atributo que no es unico.
 * Como no es unico un solo since/next es suficiente para paginar por los elementos repetidos.
 * Se utiliza en conjunto con los Id de los item para poder paginar.
 * Ej, atributo.
 * @param <T> El tipo del Id.
 * @param <K> El tipo del atributo por el cual se pagina.
 * 
 */
public abstract class PaginationDataDual<T,K> extends PaginationData<T> {

    private K since;
    private K next;
    public K getSince() {
        return since;
    }
    public void setSince(K since) {
        this.since = since;
    }
    public K getNext() {
        return next;
    }
    public void setNext(K next) {
        this.next = next;
    }
    public abstract boolean hasNext();
    public abstract boolean hasSince();
}
