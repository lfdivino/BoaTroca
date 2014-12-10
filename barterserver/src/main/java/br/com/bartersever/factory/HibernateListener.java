/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.bartersever.factory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Date;

/**
 * Web application lifecycle listener.
 *
 * @author Andre
 */
public class HibernateListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        HibernateFactory.createSessionFactory();
        sce.getServletContext().log("Session Factory Inicializada: " + new Date());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        HibernateFactory.getSessionFactory().close();
        sce.getServletContext().log("Session Factory Destruida: " + new Date());
    }
}
