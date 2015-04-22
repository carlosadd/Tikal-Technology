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
package technology.tikal.string.util;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author Nekorp
 *
 */
public class StringNormalizer {

    private static final Pattern UNDESIRABLES = Pattern.compile("\\p{Punct}");
    
    private StringNormalizer() {
        
    }
    /**
     * remplazar acentos
     * todo a minusculas
     * quita los espacios extras.
     * elimina los signos de puntuacion: !"#$%&'()*+,-./:;<=>?@[\]^_`{|}~
     */
    public static String normalize(String input) {
        if (input == null) {
            return null;
        }
        String result = input;
        result = UNDESIRABLES.matcher(result).replaceAll("");
        result = result.toLowerCase();
        result = StringUtils.normalizeSpace(result);
        result = StringUtils.replaceChars(result, '\u00E1', 'a');
        result = StringUtils.replaceChars(result, '\u00ED', 'i');
        result = StringUtils.replaceChars(result, '\u00FA', 'u');
        result = StringUtils.replaceChars(result, '\u00E9', 'e');
        result = StringUtils.replaceChars(result, '\u00F3', 'o');
        return result;
    }
}
