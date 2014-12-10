/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.barterserver.model;

/**
 *
 * @author guilherme
 */
public class SearchJSON {
    
    private Long pictureId;
    private String pictureTitle;
    private String picturePhotoURL;
    private Long ownerId;
    private String ownerName;
    private String ownerEmail;
    private Double distance;
    
    public SearchJSON(Long pictureId, String pictureTitle, String picturePhotoURL, Long ownerId, String ownerName, String ownerEmail, Double distance){
        
        this.pictureId = pictureId;
        this.pictureTitle = pictureTitle;
        this.picturePhotoURL = picturePhotoURL;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.ownerEmail = ownerEmail;
        this.distance = distance;
    }

    public Long getPictureId() {
        return pictureId;
    }

    public void setPictureId(Long pictureId) {
        this.pictureId = pictureId;
    }

    public String getPictureTitle() {
        return pictureTitle;
    }

    public void setPictureTitle(String pictureTitle) {
        this.pictureTitle = pictureTitle;
    }

    public String getPicturePhotoURL() {
        return picturePhotoURL;
    }

    public void setPicturePhotoURL(String picturePhotoURL) {
        this.picturePhotoURL = picturePhotoURL;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
    
    
    
}
