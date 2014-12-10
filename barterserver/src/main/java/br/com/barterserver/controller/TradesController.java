/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.barterserver.controller;

import br.com.barterserver.dao.PictureDAO;
import br.com.barterserver.dao.TradeDAO;
import br.com.barterserver.dao.UserDAO;
import br.com.barterserver.model.Status;
import br.com.barterserver.model.Trade;
import br.com.barterserver.model.TradeJSON;
import br.com.barterserver.model.User;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.RequestInfo;
import br.com.caelum.vraptor.http.route.Router;
import br.com.caelum.vraptor.resource.HttpMethod;
import br.com.caelum.vraptor.view.Results;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 *
 * @author guilherme
 */
@Resource
public class TradesController {
    
    private TradeDAO dao;
    private Result result;
    private Router router;
    private RequestInfo requestInfo;
    private UserDAO userDAO;
    private PictureDAO pictureDAO;
    
    public TradesController(Result result, TradeDAO dao, Router router, RequestInfo requestInfo, UserDAO userDAO, PictureDAO pictureDAO){
        
        this.dao = dao;
        this.result = result;
        this.router = router;
        this.requestInfo = requestInfo;
        this.userDAO = userDAO;
        this.pictureDAO = pictureDAO;
    }
    
    @Path("trade/post/list")
    public void listTrades(User user){
        
        //----------------HTTP HEADER NEVER CHANGE----------------------//
        Set<HttpMethod> allowed = router.allowedMethodsFor(requestInfo.getRequestedUri());
        result.use(Results.status()).header("Allow", allowed.toString().replaceAll("\\[|\\]", ""));  
        result.use(Results.status()).header("Access-Control-Allow-Origin", "*");           
        result.use(Results.status()).header("Access-Control-Allow-Methods", allowed.toString().replaceAll("\\[|\\]", ""));           
        result.use(Results.status()).header("Access-Control-Allow-Headers", "Content-Type, accept, authorization, origin");
        //----------------HTTP HEADER NEVER CHANGE----------------------//         
            user = userDAO.findById(user.getId());
            List<TradeJSON> tradesJSON = new ArrayList<TradeJSON>();
            List<Trade> listOfTrades = dao.listMyTrades(user);
            for(Trade t: listOfTrades){
                tradesJSON.add(new TradeJSON(t.getId(),t.getPictureOffering().getId(), t.getPictureOffering().getTitle(),t.getUserRequiring().getId(), t.getUserRequiring().getName(), t.getStatus().toString()));
            }
            result.use(Results.json()).withoutRoot().from(tradesJSON).serialize();
    }
    
    
    @Path("trade/post/new")
    public void keepTrade(Trade trade){
        
        //----------------HTTP HEADER NEVER CHANGE----------------------//
        Set<HttpMethod> allowed = router.allowedMethodsFor(requestInfo.getRequestedUri());
        result.use(Results.status()).header("Allow", allowed.toString().replaceAll("\\[|\\]", ""));  
        result.use(Results.status()).header("Access-Control-Allow-Origin", "*");           
        result.use(Results.status()).header("Access-Control-Allow-Methods", allowed.toString().replaceAll("\\[|\\]", ""));           
        result.use(Results.status()).header("Access-Control-Allow-Headers", "Content-Type, accept, authorization, origin");
        //----------------HTTP HEADER NEVER CHANGE----------------------//        
        
        trade.setUserOffering(userDAO.findById(trade.getUserOffering().getId()));
        trade.setUserRequiring(userDAO.findById(trade.getUserRequiring().getId()));
        trade.setPictureOffering(pictureDAO.findById(trade.getPictureOffering().getId()));
        trade.setStatus(Status.PEDING);
        trade.setDataTrade(new Date());
        Trade t = dao.saveOrUpdateAndReturn(trade);
        result.use(Results.json()).withoutRoot().from(t).serialize();
    }
    
    @Path("trade/post/update")
    public void updateTrade(Trade trade){
        
        //----------------HTTP HEADER NEVER CHANGE----------------------//
        Set<HttpMethod> allowed = router.allowedMethodsFor(requestInfo.getRequestedUri());
        result.use(Results.status()).header("Allow", allowed.toString().replaceAll("\\[|\\]", ""));  
        result.use(Results.status()).header("Access-Control-Allow-Origin", "*");           
        result.use(Results.status()).header("Access-Control-Allow-Methods", allowed.toString().replaceAll("\\[|\\]", ""));           
        result.use(Results.status()).header("Access-Control-Allow-Headers", "Content-Type, accept, authorization, origin");
        //----------------HTTP HEADER NEVER CHANGE----------------------//        
        Trade tr = dao.findById(trade.getId());
        tr.setPictureRequiring(pictureDAO.findById(trade.getPictureRequiring().getId()));
        tr.setStatus(Status.PROCESSING);
        Trade t = dao.saveOrUpdateAndReturn(tr);
        result.use(Results.json()).withoutRoot().from(t).serialize();
    }
    
    @Path("trade/post/complete")
    public void completeTrade(Trade trade){
        
        //----------------HTTP HEADER NEVER CHANGE----------------------//
        Set<HttpMethod> allowed = router.allowedMethodsFor(requestInfo.getRequestedUri());
        result.use(Results.status()).header("Allow", allowed.toString().replaceAll("\\[|\\]", ""));  
        result.use(Results.status()).header("Access-Control-Allow-Origin", "*");           
        result.use(Results.status()).header("Access-Control-Allow-Methods", allowed.toString().replaceAll("\\[|\\]", ""));           
        result.use(Results.status()).header("Access-Control-Allow-Headers", "Content-Type, accept, authorization, origin");
        //----------------HTTP HEADER NEVER CHANGE----------------------//       
        
        trade.setStatus(Status.COMPLETE);
        dao.saveOrUpdate(trade);
        Trade t = dao.saveOrUpdateAndReturn(trade);
        result.use(Results.json()).withoutRoot().from(t).serialize();
    }
    
}
