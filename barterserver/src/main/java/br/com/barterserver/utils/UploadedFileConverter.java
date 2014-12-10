/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.barterserver.utils;

import br.com.caelum.vraptor.Convert;
import br.com.caelum.vraptor.Converter;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author guilherme
 */
@Convert(UploadedFile.class)  
public class UploadedFileConverter implements Converter<UploadedFile>{  
      
    private final HttpServletRequest request;  
      
    public UploadedFileConverter(HttpServletRequest request) {  
        this.request = request;  
    }  
      
    public UploadedFile convert(String value, Class<? extends UploadedFile> type, ResourceBundle bundle) {  
        Object upload = request.getAttribute(value);  
        return upload == null ? null :type.cast(upload);  
    }  
} 