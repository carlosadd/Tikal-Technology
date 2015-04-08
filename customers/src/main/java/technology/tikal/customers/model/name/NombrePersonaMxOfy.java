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
package technology.tikal.customers.model.name;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import technology.tikal.customers.model.OfyEntity;

import com.googlecode.objectify.annotation.Subclass;

/**
 * 
 * @author Nekorp
 *
 */
@Subclass
public class NombrePersonaMxOfy extends NombrePersonaMx implements OfyEntity<NombrePersonaMx> {

    private NombrePersonaMxOfy() {
        
    }
    public NombrePersonaMxOfy(NombrePersonaMx source) {
        this();
        this.update(source);
    }
    @Override
    public String toString() {
        return StringUtils.join(new String[]{
                this.getPrimerNombre()," ",
                this.getSegundoNombre()," ",
                this.getApellidoPaterno()," ",
                this.getApellidoMaterno()
        });
    }
    @Override
    public void update(NombrePersonaMx source) {
        BeanUtils.copyProperties(source, this);
    }
    
}
