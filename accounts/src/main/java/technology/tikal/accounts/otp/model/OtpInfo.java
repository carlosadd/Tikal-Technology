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
package technology.tikal.accounts.otp.model;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
/**
 * 
 * @author Nekorp
 *
 */
public class OtpInfo {    
    private String secret;
    private boolean enabled;
    private List<Integer> scratchCodes;
    public OtpInfo() {
        scratchCodes = new LinkedList<>();
        secret = "";
    }
    public String getSecret() {
        return secret;
    }
    public void setSecret(String secret) {
        this.secret = secret;
    }
    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public List<Integer> getScratchCodes() {
        return scratchCodes;
    }
    public boolean hasOtp() {
        return StringUtils.isNotEmpty(secret);
    }
    public void clear() {
        scratchCodes.clear();
        secret = "";
        enabled = false;
    }
}
