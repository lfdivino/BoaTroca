/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.bartersever.factory;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.stat.Statistics;

/**
 * @author Andre
 */
public class HibernateFactory {

    private static SessionFactory sessionFactory;

    public static SessionFactory createSessionFactory() {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return sessionFactory;
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory != null ? sessionFactory : createSessionFactory();
    }

    public static Session getSession() throws HibernateException {
        try {
            return getSessionFactory().openSession();
        } catch (HibernateException e) {
            throw new HibernateException("Erro ao obter uma session a partir da SessionFactory: ", e);
        }
    }

    public static Statistics getStatistics() {
        if (!getSessionFactory().getStatistics().isStatisticsEnabled()) {
            getSessionFactory().getStatistics().setStatisticsEnabled(true);
        }
        return getSessionFactory().getStatistics();
    }
}
