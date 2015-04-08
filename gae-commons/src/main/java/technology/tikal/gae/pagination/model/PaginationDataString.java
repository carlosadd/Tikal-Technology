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
 * @author Nekorp
 */
public class PaginationDataString extends PaginationData<String> {

    /* (non-Javadoc)
     * @see org.nekorp.workflow.backend.data.pagination.model.PaginationData#hasNext()
     */
    @Override
    public boolean hasNextId() {
        return !StringUtils.isEmpty(this.getNextId());
    }

    @Override
    public boolean hasSinceId() {
        return !StringUtils.isEmpty(this.getSinceId());
    }

}
