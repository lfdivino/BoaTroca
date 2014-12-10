/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.barterserver.model;

/**
 *
 * @author Luciano Pucci
 */
public class TradeJSON {
    
    private Long tradeId;
    private Long pictureId;
    private String pictureTitle;
    private Long userReqId;
    private String userReqName;
    private String tradeStatus;
    
    public TradeJSON(Long tradeId, Long pictureId, String pictureTitle, Long userReqId, String userReqName, String tradeStatus){
        this.pictureId = pictureId;
        this.tradeId = tradeId;
        this.pictureTitle = pictureTitle;
        this.userReqId = userReqId;
        this.userReqName = userReqName;
        this.tradeStatus = tradeStatus;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
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

    public Long getUserReqId() {
        return userReqId;
    }

    public void setUserReqId(Long userReqId) {
        this.userReqId = userReqId;
    }

    public String getUserReqName() {
        return userReqName;
    }

    public void setUserReqName(String userReqName) {
        this.userReqName = userReqName;
    }
    
    
    
}
