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
package technology.tikal.accounts.model.config;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * 
 * @author Nekorp
 *
 */
@Entity
@Cache
public class SessionDaoRestConfig {

    @Id
    private String id;
    private List<SessionDaoRestConfigEntry> configInfo;
    
    private SessionDaoRestConfig() {
        configInfo = new ArrayList<>();
    }
    
    public SessionDaoRestConfig(String id) {
        this();
        this.id = id;
    }

    public List<SessionDaoRestConfigEntry> getConfigInfo() {
        return configInfo;
    }

    public void setConfigInfo(List<SessionDaoRestConfigEntry> configInfo) {
        this.configInfo = configInfo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
