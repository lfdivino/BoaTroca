/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.barterserver.model;

/**
 *
 * @author fdxdiogo
 */
public class ChatJSON {
    
    private String message;
    private String userName;
    private StatusMessages status;
    
    public ChatJSON(String userName, String message, StatusMessages status){
        this.message = message;
        this.userName = userName;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public StatusMessages getStatus() {
        return status;
    }

    public void setStatus(StatusMessages status) {
        this.status = status;
    }
    
    
    
}
