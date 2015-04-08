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

/**
 * 
 * @author Nekorp
 *
 */
public class NameFactory {

    public static Name buildInternal(Name source, boolean indexed) {
        if (source == null) {
            return null;
        }
        if (source instanceof NombrePersonaMx) {
            if(indexed) {
                return new NombrePersonaMxIndexed((NombrePersonaMx)source);
            } else {
                return new NombrePersonaMxOfy((NombrePersonaMx)source);
            }
        }
        if (source instanceof NombrePersonaSimpleMx) {
            if(indexed) {
                return new NombrePersonaSimpleMxIndexed((NombrePersonaSimpleMx)source);
            } else {
                return new NombrePersonaSimpleMxOfy((NombrePersonaSimpleMx)source);
            }
        }
        if (source instanceof OrganizationName) {
            if(indexed) {
                return new OrganizationNameIndexed((OrganizationName)source);
            } else {
                return new OrganizationNameOfy((OrganizationName)source);
            }
        }
        throw new IllegalArgumentException("Tipo no soportado");
    }
}
