/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.barterserver.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author guilherme
 */
@Entity
@Table(name = "trades")
public class Trade implements Serializable{
    
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private User userOffering;
    @ManyToOne
    private User userRequiring;
    @ManyToOne
    private Picture pictureOffering;
    @ManyToOne
    private Picture pictureRequiring;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Temporal(TemporalType.DATE)
    private Date dataTrade;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUserOffering() {
        return userOffering;
    }

    public void setUserOffering(User userOffering) {
        this.userOffering = userOffering;
    }

    public User getUserRequiring() {
        return userRequiring;
    }

    public void setUserRequiring(User userRequiring) {
        this.userRequiring = userRequiring;
    }

    public Picture getPictureOffering() {
        return pictureOffering;
    }

    public void setPictureOffering(Picture pictureOffering) {
        this.pictureOffering = pictureOffering;
    }

    public Picture getPictureRequiring() {
        return pictureRequiring;
    }

    public void setPictureRequiring(Picture pictureRequiring) {
        this.pictureRequiring = pictureRequiring;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getDataTrade() {
        return dataTrade;
    }

    public void setDataTrade(Date dataTrade) {
        this.dataTrade = dataTrade;
    }
    
}
