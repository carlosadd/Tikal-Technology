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
package technology.tikal.gae.service.template;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import technology.tikal.gae.error.exceptions.MessageSourceResolvableException;
import technology.tikal.gae.error.exceptions.NotValidException;
import technology.tikal.gae.error.model.BasicErrorMessage;

import com.googlecode.objectify.NotFoundException;

/**
 * 
 * @author Nekorp
 *
 */
public class RestControllerTemplate {
    private final Log logger = LogFactory.getLog(getClass());
    private AbstractMessageSource messageSource;
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BasicErrorMessage handleException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        if (this.logger.isInfoEnabled()) {
            ex.printStackTrace();
        }
        response.setHeader("Content-Type","application/json;charset=UTF-8");
        BasicErrorMessage result = new BasicErrorMessage();
        String[] msg = new String[] {ex.getMessage()};
        result.setType(ex.getClass().getSimpleName());
        result.setMessage(msg);
        return result;
    }
    
    @ExceptionHandler(NotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BasicErrorMessage handleException(NotValidException ex, HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Content-Type","application/json;charset=UTF-8");
        BasicErrorMessage result = new BasicErrorMessage();
        BindingResult detail = ex.getDetails();
        String[] msg = new String[detail.getAllErrors().size()];
        String[][] code = new String[detail.getAllErrors().size()][];
        String[][] args = new String[detail.getAllErrors().size()][];
        int index = 0;
        Locale locale = LocaleContextHolder.getLocale();
        for(ObjectError x: detail.getAllErrors()) {
            Object[] tmpArgs = resolveArgumentMessage(x.getCodes(), x.getArguments());
            msg[index] = getValidationMessage(x.getDefaultMessage(), x.getCodes(), tmpArgs, locale);
            code[index] = x.getCodes();
            String[] tmp = new String[tmpArgs.length];
            args[index] = tmp;
            int j = 0;
            for(Object y : tmpArgs) {
                args[index][j] = y.toString();
                j = j + 1;
            }
            index = index + 1;
        }
        result.setType(ex.getClass().getSimpleName());
        result.setMessage(msg);
        result.setCode(code);
        result.setArguments(args);
        return result;
    }
    
    private Object[] resolveArgumentMessage(String[] codes, Object[] arguments) {
        String arrayNumber = "";
        Object[] args = arguments;
        int i = 0;
        while (arrayNumber.length() == 0 && i < codes.length) {
            arrayNumber = getValidationArrayNumber(codes[i]);
            i = i + 1;
        }
        if (arrayNumber.length() > 0) {
            args = new Object[arguments.length + 1];
            args[0] = arrayNumber;
            int j = 1;
            for(Object x: arguments) {
                args[j] = x;
                j = j + 1;
            }
        }
        return args;
    }
    
    private String getValidationMessage(String defaultMessage, String[] codes, Object[] arguments, Locale locale) {
        if (codes.length > 0) {
            String message = "";
            int i = 0;
            while (message.length() == 0 && i < codes.length) {
                message = messageSource.getMessage(codes[i], arguments, "", locale);
                i = i + 1;
            }
            if(message.length() == 0) {
                message = defaultMessage;
            }
            
            return message;
        } else {
            return defaultMessage;
        }
    }
    
    private String getValidationArrayNumber(String param) {
        String result = "";
        int start = 0;
        int openingCount = 0;
        for(int index = 0; index < param.length(); index++) {
            if (openingCount > 0) {
                if (param.charAt(index) == ']') {
                    openingCount = openingCount - 1;
                    if (openingCount == 0) {
                        result = StringUtils.substring(param, start + 1, index);
                    }
                }
                if (param.charAt(index) == '[') {
                    openingCount = openingCount + 1;
                }
            }
            else {
                if (param.charAt(index) == '[') {
                    start = index;
                    openingCount = 1;
                }
            }
        }
        return result;
    }
    
    @ExceptionHandler({MessageSourceResolvableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BasicErrorMessage handleMsgException(MessageSourceResolvableException ex, HttpServletRequest request, HttpServletResponse response) {
        if (this.logger.isInfoEnabled()) {
            ex.printStackTrace();
        }
        response.setHeader("Content-Type","application/json;charset=UTF-8");
        DefaultMessageSourceResolvable detail = ex.getMsgResolve();
        Locale locale = LocaleContextHolder.getLocale();
        BasicErrorMessage result = new BasicErrorMessage();
        String[] msg = new String[]{this.messageSource.getMessage(detail.getCode(), detail.getArguments(), "", locale)};
        String[][] code = new String[1][];
        code[0] = detail.getCodes();
        String[][] args = new String[1][];
        String[] tmpArgs = new String[detail.getArguments().length];
        int index = 0;
        for (Object x: detail.getArguments()) {
            tmpArgs[index] = x.toString();
            index = index + 1;
        }
        args[0] = tmpArgs;
        result.setType(ex.getClass().getSimpleName());
        result.setMessage(msg);
        result.setCode(code);
        result.setArguments(args);
        return result;
    }
    
    @ExceptionHandler({IllegalArgumentException.class, HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BasicErrorMessage handleBadRequestException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        if (this.logger.isInfoEnabled()) {
            ex.printStackTrace();
        }
        response.setHeader("Content-Type","application/json;charset=UTF-8");
        BasicErrorMessage result = new BasicErrorMessage();
        String[] msg = new String[] {ex.getMessage()};
        result.setType(ex.getClass().getSimpleName());
        result.setMessage(msg);
        return result;
    }
    
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BasicErrorMessage handleException(NotFoundException ex, HttpServletRequest request, HttpServletResponse response) {
        if (this.logger.isInfoEnabled()) {
            ex.printStackTrace();
        }
        response.setHeader("Content-Type","application/json;charset=UTF-8");
        BasicErrorMessage result = new BasicErrorMessage();
        String[] msg = new String[] {ex.getMessage()};
        result.setType(ex.getClass().getSimpleName());
        result.setMessage(msg);
        return result;
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public void handleAccessDenied(AccessDeniedException ex, HttpServletRequest request, HttpServletResponse response) {
        if (this.logger.isInfoEnabled()) {
            ex.printStackTrace();
        }
    }

    public void setMessageSource(AbstractMessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
