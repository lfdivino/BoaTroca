/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.barterserver.controller;

import br.com.barterserver.dao.ChatDAO;
import br.com.barterserver.dao.TradeDAO;
import br.com.barterserver.dao.UserDAO;
import br.com.barterserver.model.Chat;
import br.com.barterserver.model.ChatJSON;
import br.com.barterserver.model.Status;
import br.com.barterserver.model.StatusMessages;
import br.com.barterserver.model.Trade;
import br.com.barterserver.model.User;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.RequestInfo;
import br.com.caelum.vraptor.http.route.Router;
import br.com.caelum.vraptor.resource.HttpMethod;
import br.com.caelum.vraptor.view.Results;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author guilherme
 */
@Resource
public class ChatsController{
    
    private ChatDAO dao;
    private Result result;
    private Router router;
    private RequestInfo requestInfo;
    private UserDAO userDAO;
    private TradeDAO tradeDAO;
    
    public ChatsController(ChatDAO dao, Result result, Router router, RequestInfo requestInfo, UserDAO userDAO, TradeDAO tradeDAO){
        this.dao = dao;
        this.result = result;
        this.router = router;
        this.requestInfo = requestInfo;
        this.tradeDAO = tradeDAO;
        this.userDAO = userDAO;
    }
    
    @Path("/user/chat")
    public void listChat(Trade trade, User user){ 
        
        //----------------HTTP HEADER NEVER CHANGE----------------------//
        Set<HttpMethod> allowed = router.allowedMethodsFor(requestInfo.getRequestedUri());
        result.use(Results.status()).header("Allow", allowed.toString().replaceAll("\\[|\\]", ""));  
        result.use(Results.status()).header("Access-Control-Allow-Origin", "*");           
        result.use(Results.status()).header("Access-Control-Allow-Methods", allowed.toString().replaceAll("\\[|\\]", ""));           
        result.use(Results.status()).header("Access-Control-Allow-Headers", "Content-Type, accept, authorization, origin");
        //----------------HTTP HEADER NEVER CHANGE----------------------//
        
        boolean isThereNewMessages = false;
        
        List<Chat> listChats = dao.getChatMessages(trade.getId());
        List<ChatJSON> chatsJSON = new ArrayList<ChatJSON>();
        for(Chat c: listChats){
            ChatJSON chatJSON = new ChatJSON(c.getUser().getName(), c.getMessage(), c.getStatus());
            chatsJSON.add(chatJSON);
            if(c.getStatus() == StatusMessages.NEW){
                isThereNewMessages = true;
                c.setStatus(StatusMessages.OLD);
                dao.saveOrUpdate(c);
            }
        }
        
        
            result.use(Results.json()).withoutRoot().from(chatsJSON).serialize();
        
    }
    
    @Post("/user/new/message")
    public void newMessageToTrade(Chat chat){
        
        //----------------HTTP HEADER NEVER CHANGE----------------------//
        Set<HttpMethod> allowed = router.allowedMethodsFor(requestInfo.getRequestedUri());
        result.use(Results.status()).header("Allow", allowed.toString().replaceAll("\\[|\\]", ""));  
        result.use(Results.status()).header("Access-Control-Allow-Origin", "*");           
        result.use(Results.status()).header("Access-Control-Allow-Methods", allowed.toString().replaceAll("\\[|\\]", ""));           
        result.use(Results.status()).header("Access-Control-Allow-Headers", "Content-Type, accept, authorization, origin");
        //----------------HTTP HEADER NEVER CHANGE----------------------//
        
        chat.setUser(userDAO.findById(chat.getUser().getId()));
        chat.setTrade(tradeDAO.findById(chat.getTrade().getId())); 
        chat.setStatus(StatusMessages.NEW);
        dao.saveOrUpdate(chat);
    }
}
