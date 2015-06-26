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

import java.util.Date;
/**
 * 
 * @author Nekorp
 *
 */
public class OtpToken implements Comparable<OtpToken> {
    private Integer otp;
    private Date time;
    
    private OtpToken(){
    };
    
    public OtpToken(Integer otp){
        this();
        if (otp == null) {
            throw new IllegalArgumentException("el otp no puede ser nulo");
        }
        this.otp = otp;
        this.time = new Date();
    }
    public Integer getOtp() {
        return otp;
    }
    public Date getTime() {
        return time;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((otp == null) ? 0 : otp.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OtpToken other = (OtpToken) obj;
        if (otp == null) {
            if (other.otp != null)
                return false;
        } else if (!otp.equals(other.otp))
            return false;
        return true;
    }

    @Override
    public int compareTo(OtpToken o) {
        return this.time.compareTo(o.getTime());
    }
}
