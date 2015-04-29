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
package technology.tikal.gae.http.cache.model;

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
public class UriEtagEntry {

    @Id
    private String id;
    private String namespace;
    private String uri;
    private String etag;
    
    private UriEtagEntry(){
    }
    
    protected UriEtagEntry(String id, String namespace, String uri, String etag) {
        this();
        this.id = id;
        this.namespace = namespace;
        this.uri = uri;
        this.etag = etag;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getId() {
        return id;
    }

    public String getUri() {
        return uri;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }
    
}
