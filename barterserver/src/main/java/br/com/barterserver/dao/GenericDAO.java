package br.com.barterserver.dao;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author André
 */
public class GenericDAO<T> {

    protected Session session;
    protected Class<T> genericType;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public GenericDAO(Session session) {
        this.session = session;
        ParameterizedType par = (ParameterizedType) this.getClass()
                .getGenericSuperclass();
        genericType = (Class) par.getActualTypeArguments()[0];
    }

    public void add(T obj) {
        Transaction tx = session.beginTransaction();
        session.save(obj);
        tx.commit();
    }

    public void refresh(T obj) {
        Transaction tx = session.beginTransaction();
        session.refresh(obj);
        tx.commit();
    }

    public void update(T obj) {
        Transaction tx = session.beginTransaction();
        session.merge(obj);
        tx.commit();
    }
    
    public void saveOrUpdate(T obj) {
        Transaction tx = session.beginTransaction();
        session.saveOrUpdate(obj);
        tx.commit();
    }

    public void delete(T obj) {
        Transaction tx = session.beginTransaction();
        session.delete(obj);
        tx.commit();
    }

    @SuppressWarnings("unchecked")
    public T findById(long id) {
        return (T) session.createQuery(
                "from " + genericType.getName() + " where id = "
                + String.valueOf(id)).uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return (List<T>) session.createQuery("from " + genericType.getName())
                .list();
    }    
}
