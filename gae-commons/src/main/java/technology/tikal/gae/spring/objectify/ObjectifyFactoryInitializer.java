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
package technology.tikal.gae.spring.objectify;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
/**
 * 
 * @author Nekorp
 *
 */
public class ObjectifyFactoryInitializer implements InitializingBean {

    private final Log logger = LogFactory.getLog(getClass());

    private List<Class<?>> classes;    

    public void afterPropertiesSet() throws Exception {
        if (this.logger.isInfoEnabled()) {
            this.logger.info("Initialization started");
        }
        long startTime = System.currentTimeMillis();
        ObjectifyFactory factory = ObjectifyService.factory();
        if (classes == null) {
            classes = new ArrayList<Class<?>>();
        }

        for (Class<?> clazz : classes) {
            factory.register(clazz);
            if (this.logger.isInfoEnabled()) {
                this.logger.info("Registered entity class [" + clazz.getName()
                        + "]");
            }
        }
        ObjectifyService.setFactory(factory);
        if (this.logger.isInfoEnabled()) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            this.logger.info("Initialization completed in " + elapsedTime
                    + " ms");
        }
    }

    public void setClasses(List<Class<?>> classes) {
        this.classes = classes;
    }
}
