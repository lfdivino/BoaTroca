/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.barterserver.dao;

import br.com.barterserver.model.Chat;
import br.com.caelum.vraptor.ioc.Component;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author guilherme
 */
@Component
public class ChatDAO extends GenericDAO<Chat>{

    public ChatDAO(Session session) {
        super(session);
    }
    
    public List<Chat> getChatMessages(Long tradeId){
        
        Query q = session.createQuery("from Chat c where c.trade.id = :tradeId");
        q.setParameter("tradeId", tradeId);
        
        return q.list(); 
    }
    
}
