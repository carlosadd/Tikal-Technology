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
package technology.tikal.hibernate.validation;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * 
 * @author Nekorp
 *
 */
public class NotEmptyPojoValidator implements ConstraintValidator<NotEmptyPojo, Object> {

    private String[] attributes = new String[]{};
    @Override
    public void initialize(NotEmptyPojo constraintAnnotation) {
        attributes = constraintAnnotation.attribute();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if(value == null) {
            return false;
        }
        if (attributes.length > 0) {
            Object x;
            for(String attribute: attributes) {
                try {
                    x = PropertyUtils.getProperty(value, attribute);
                    if(x != null) {
                        return true;
                    }
                } catch (IllegalAccessException|InvocationTargetException|NoSuchMethodException e) {
                }
            }
        } else {
            PropertyDescriptor[] props = PropertyUtils.getPropertyDescriptors(value);
            Object x;
            for(PropertyDescriptor prop: props) {
                try {
                    x = PropertyUtils.getProperty(value, prop.getName());
                    if(x != null && !(x instanceof Class)) {
                        return true;
                    }
                } catch (IllegalAccessException|InvocationTargetException|NoSuchMethodException e) {
                    return false;
                }
            }
        }
        return false;
    }

}
