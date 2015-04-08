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

import org.apache.commons.lang.StringUtils;
/**
 * 
 * @author Nekorp
 *
 */
public class PaginationDataDualLongString extends PaginationDataDual<Long, String> {

    @Override
    public boolean hasNextId() {
        return this.getNextId() != null 
            && this.getNextId() != 0;
    }

    @Override
    public boolean hasSinceId() {
        return this.getSinceId() != null 
            && this.getSinceId() != 0;
    }

    @Override
    public boolean hasNext() {
        return !StringUtils.isEmpty(this.getNext());
    }

    @Override
    public boolean hasSince() {
        return !StringUtils.isEmpty(this.getSince());
    }

}
