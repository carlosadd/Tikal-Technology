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

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author Nekorp
 *
 */
public class UriEtagEntryFactory {

    private final static String DEFAULT_NAMESPACE = "DEFAULT";
    private String namespace = UriEtagEntryFactory.DEFAULT_NAMESPACE;
    private SecureRandom random;
    
    public UriEtagEntryFactory() {
        this.random = new SecureRandom();
    }
    
    public String resolveId(String uri) {
        if (StringUtils.isEmpty(namespace)) {
            throw new NullPointerException("The name space can't be null");
        }
        if (StringUtils.isEmpty(uri)) {
            throw new NullPointerException("The uri can't be null");
        }
        return namespace + ":" + uri;
    }
    
    public String buildNewEtagValue() {
        Date ahora = new Date();
        String r =Long.toString(ahora.getTime(), 32);
        r = r + new BigInteger(130, random).toString(32);
        return r;
    }
    
    public UriEtagEntry buildUriEtagEntry(String uri) {
        UriEtagEntry result = new UriEtagEntry(resolveId(uri), namespace, uri, buildNewEtagValue());
        return result;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
