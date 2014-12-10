/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.barterserver.controller;

import br.com.barterserver.dao.PictureDAO;
import br.com.barterserver.dao.TradeDAO;
import br.com.barterserver.dao.UserDAO;
import br.com.barterserver.model.Picture;
import br.com.barterserver.model.SearchJSON;
import br.com.barterserver.model.Trade;
import br.com.barterserver.model.User;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.RequestInfo;
import br.com.caelum.vraptor.http.route.Router;
import br.com.caelum.vraptor.resource.HttpMethod;
import br.com.caelum.vraptor.view.Results;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author guilherme
 */
@Resource
public class UsersController {
    
    private UserDAO dao;
    private PictureDAO picDAO;
    private Result result;
    private Router router;
    private RequestInfo requestInfo;
    private TradeDAO tradeDAO;
    
    public UsersController(Result result, UserDAO dao, PictureDAO picDAO, Router router, RequestInfo requestInfo, TradeDAO tradeDAO){
        
        this.result = result;
        this.dao = dao;
        this.requestInfo = requestInfo;
        this.router = router;
        this.picDAO = picDAO;
        this.tradeDAO = tradeDAO;
        
    }
    
    public void index(){
        result.include("users",dao.findAll());
    }
    
    @Path("/user/{id}")
    public void view(Long id){
        result.include("user",dao.findById(id));
    }
    
    @Path("/user/add")
    public void add(){
        
    }
    
    @Path("/user/{id}")
    public void edit(Long id){
        result.include("user",dao.findById(id));
    }
    
    @Path("/search")
    public void findTrades(String title, User currentUser){
        
        //----------------HTTP HEADER NEVER CHANGE----------------------//
        Set<HttpMethod> allowed = router.allowedMethodsFor(requestInfo.getRequestedUri());
        result.use(Results.status()).header("Allow", allowed.toString().replaceAll("\\[|\\]", ""));  
        result.use(Results.status()).header("Access-Control-Allow-Origin", "*");           
        result.use(Results.status()).header("Access-Control-Allow-Methods", allowed.toString().replaceAll("\\[|\\]", ""));           
        result.use(Results.status()).header("Access-Control-Allow-Headers", "Content-Type, accept, authorization, origin");
        //----------------HTTP HEADER NEVER CHANGE----------------------//         
        
        currentUser = dao.findById(currentUser.getId());
        
        List<Picture> pics = dao.searchPictures(title);
        List<SearchJSON> searchs = new ArrayList<SearchJSON>();
        List<Trade> trades = tradeDAO.listMyTrades(currentUser);
        
        for(Picture p: pics ){
            for(Trade t: trades){
                if(t.getPictureOffering().getId() != p.getId() && p.getOwner().getId() != currentUser.getId()){
                    SearchJSON s = new SearchJSON(p.getId(), p.getTitle(), p.getPhotoURL(), p.getOwner().getId(), p.getOwner().getName(), p.getOwner().getEmail(), this.distance(p.getOwner().getLoc_lat(), p.getOwner().getLoc_long(), currentUser.getLoc_lat(), currentUser.getLoc_long()));
                    searchs.add(s);
                }
            }
        }
        
        Collections.sort(searchs, new Comparator<SearchJSON>() {

            @Override
            public int compare(SearchJSON o1, SearchJSON o2) {
                return (int) (o1.getDistance() - o1.getDistance());
            }
    
        });
        
        result.use(Results.json()).withoutRoot().from(searchs).serialize();
        
    }
     
   public Double distance(Double targetLat, Double targetLong, Double userLat, Double userLong){
       
       try{
       
        Double R = 6371d;
        
        Double dLat = Math.toRadians(targetLat-userLat);
        Double dLon = Math.toRadians(targetLong-userLong);
        Double targetLatr = Math.toRadians(targetLat);
        Double userLatr = Math.toRadians(userLat);
        
        Double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(targetLatr) * Math.cos(userLatr);
        
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
        
        Double distance = R * c;
        return distance;
        
       }catch(NullPointerException e){
           return 100000d;
       }
        
        
    }
    
    @Path("/user/post/mypictures")
    public void listMyPictures(User user){
        
        //----------------HTTP HEADER NEVER CHANGE----------------------//
        Set<HttpMethod> allowed = router.allowedMethodsFor(requestInfo.getRequestedUri());
        result.use(Results.status()).header("Allow", allowed.toString().replaceAll("\\[|\\]", ""));  
        result.use(Results.status()).header("Access-Control-Allow-Origin", "*");           
        result.use(Results.status()).header("Access-Control-Allow-Methods", allowed.toString().replaceAll("\\[|\\]", ""));           
        result.use(Results.status()).header("Access-Control-Allow-Headers", "Content-Type, accept, authorization, origin");
        //----------------HTTP HEADER NEVER CHANGE----------------------// 
       
       user = dao.findById(user.getId());
       List<Picture> myPics = user.getPictures();
       result.use(Results.json()).withoutRoot().from(myPics).serialize();
    }
   
    @Path("/user/post/picture/add")
    public void addPictureWithoutPicture(Picture picture, User user){
        
        //----------------HTTP HEADER NEVER CHANGE----------------------//
        Set<HttpMethod> allowed = router.allowedMethodsFor(requestInfo.getRequestedUri());
        result.use(Results.status()).header("Allow", allowed.toString().replaceAll("\\[|\\]", ""));  
        result.use(Results.status()).header("Access-Control-Allow-Origin", "*");           
        result.use(Results.status()).header("Access-Control-Allow-Methods", allowed.toString().replaceAll("\\[|\\]", ""));           
        result.use(Results.status()).header("Access-Control-Allow-Headers", "Content-Type, accept, authorization, origin");
        //----------------HTTP HEADER NEVER CHANGE----------------------//
        
        if(picture.getId() == null){
            user = dao.findById(user.getId());
            List<Picture> pictures = user.getPictures();
            picture.setOwner(user);
            pictures.add(picture);
            user.setPictures(pictures);
            dao.saveOrUpdate(user);
            result.use(Results.json()).withoutRoot().from(picture).serialize();
        }else{
            Picture newPic = picDAO.findById(picture.getId());
            newPic.setPhotoURL(picture.getPhotoURL());
            picDAO.saveOrUpdate(newPic);
        }
    }
    
}
