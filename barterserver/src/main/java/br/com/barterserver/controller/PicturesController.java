/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.barterserver.controller;

import br.com.barterserver.dao.PictureDAO;
import br.com.barterserver.dao.UserDAO;
import br.com.barterserver.model.Picture;
import br.com.barterserver.model.User;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.RequestInfo;
import br.com.caelum.vraptor.http.route.Router;
import br.com.caelum.vraptor.resource.HttpMethod;
import br.com.caelum.vraptor.view.Results;
import java.util.List;
import java.util.Set;

/**
 *
 * @author guilherme
 */
@Resource
public class PicturesController {
    
    private Result result;
    private PictureDAO dao;
    private UserDAO userDAO;
    private Router router;
    private RequestInfo requestInfo;
    
    public PicturesController(Result result, PictureDAO dao, UserDAO userDAO, Router router, RequestInfo requestInfo){
        this.dao = dao;
        this.result = result;
        this.userDAO = userDAO;
        this.router = router;
        this.requestInfo = requestInfo;
    }
    
    public void save(Picture picture){
        dao.saveOrUpdate(picture);
    }
    
    @Path("user/picture/delete")
    public void deleteMyPicture(Picture picture, User user){
        
        //----------------HTTP HEADER NEVER CHANGE----------------------//
        Set<HttpMethod> allowed = router.allowedMethodsFor(requestInfo.getRequestedUri());
        result.use(Results.status()).header("Allow", allowed.toString().replaceAll("\\[|\\]", ""));  
        result.use(Results.status()).header("Access-Control-Allow-Origin", "*");           
        result.use(Results.status()).header("Access-Control-Allow-Methods", allowed.toString().replaceAll("\\[|\\]", ""));           
        result.use(Results.status()).header("Access-Control-Allow-Headers", "Content-Type, accept, authorization, origin");
        //----------------HTTP HEADER NEVER CHANGE----------------------//        
            user = userDAO.findById(user.getId());
            List<Picture> listMyPics = user.getPictures();
            for(Picture p: listMyPics){
                if(p.getId() == picture.getId()){
                    listMyPics.remove(p);
                    break;
                }
            }
            user.setPictures(listMyPics);
            userDAO.saveOrUpdate(user);
            result.use(Results.http()).body("Picture DELETED");
    }
    
}
