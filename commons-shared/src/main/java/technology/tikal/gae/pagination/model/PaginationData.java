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

import javax.validation.constraints.Min;

/**
 * @author Nekorp
 */
public abstract class PaginationData<T> {
    
    private T sinceId;
    @Min(0)
    private int maxResults;
    private T nextId;
    
    public T getSinceId() {
        return sinceId;
    }
    public void setSinceId(T sinceId) {
        this.sinceId = sinceId;
    }
    public int getMaxResults() {
        return maxResults;
    }
    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }
    public T getNextId() {
        return nextId;
    }
    public void setNextId(T nextId) {
        this.nextId = nextId;
    }
    public abstract boolean hasNextId();
    public abstract boolean hasSinceId();
    @Override
    public String toString() {
        return "PaginationData: sinceId:" + sinceId
                + " maxResults:" + maxResults
                + " nextId:" + nextId;
    }
}
