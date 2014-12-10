/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.barterserver.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author guilherme
 */
@Entity
@Table(name = "chats")
public class Chat implements Serializable{
    
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Trade trade;
    @ManyToOne
    private User user;
    @Column(columnDefinition = "text")
    private String message;
    @Enumerated(EnumType.STRING)
    private StatusMessages status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public StatusMessages getStatus() {
        return status;
    }

    public void setStatus(StatusMessages status) {
        this.status = status;
    }
    
    
    
}
