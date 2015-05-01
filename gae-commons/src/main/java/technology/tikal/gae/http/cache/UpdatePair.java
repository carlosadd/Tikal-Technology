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
package technology.tikal.gae.http.cache;

/**
 * 
 * @author Nekorp
 *
 * @param <T>
 */
public class UpdatePair<T> {

    private T original;
    private T updated;
    
    public UpdatePair() {
        super();
    }
    public UpdatePair(T original, T updated) {
        super();
        this.original = original;
        this.updated = updated;
    }
    public T getOriginal() {
        return original;
    }
    public void setOriginal(T original) {
        this.original = original;
    }
    public T getUpdated() {
        return updated;
    }
    public void setUpdated(T updated) {
        this.updated = updated;
    }
}
