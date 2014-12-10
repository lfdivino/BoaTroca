/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.barterserver.dao;

import br.com.barterserver.model.Picture;
import br.com.caelum.vraptor.ioc.Component;
import org.hibernate.Session;

/**
 *
 * @author guilherme
 */
@Component
public class PictureDAO extends GenericDAO<Picture>{

    public PictureDAO(Session session) {
        super(session);
    }
    
}
