/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.barterserver.dao;

import br.com.barterserver.model.Trade;
import br.com.barterserver.model.User;
import br.com.caelum.vraptor.ioc.Component;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author guilherme
 */
@Component
public class TradeDAO extends GenericDAO<Trade>{

    public TradeDAO(Session session) {
        super(session);
    }
    
    public Trade saveOrUpdateAndReturn(Trade trade){
         
        this.saveOrUpdate(trade);
        
        return trade;
    }

    public List<Trade> listMyTrades(User user) {
       Query q = session.createQuery("from Trade t where t.userOffering = :user_offering or t.userRequiring = :user_requiring");
       q.setParameter("user_offering", user);
       q.setParameter("user_requiring", user);
       
       return q.list();
    }
    
}
