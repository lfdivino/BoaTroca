/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.barterserver.dao;
import br.com.barterserver.model.Picture;
import br.com.barterserver.model.User;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
import br.com.caelum.vraptor.ioc.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author guilherme
 */
@Component
public class UserDAO extends GenericDAO<User>{
    
    public static String PATH_PHOTO="http://localhost:8080/users/userpicsid/";

    public UserDAO(Session session) {
        super(session);
    }
    
    public User saveOrUpdateAndReturn(User user){
        this.saveOrUpdate(user);
        
        return user;
    }
    
    public User getUserByCredentials(String email, String password){
        
        Query q = session.createQuery("from User u where u.email = :email and u.password = :password ");
        q.setParameter("email", email);
        q.setParameter("password", password);
        
        return (User) q.uniqueResult();
    }
    
    public List<Picture> searchPictures(String title){
        
        Query q = session.createQuery("from Picture p where p.title = :title ");
        q.setParameter("title", title);
        
        List<Picture> pictures = q.list();
        return pictures;
    }
    
    public User getUserByEmail(String email){
        
        Query q = session.createQuery("from User where u.email = :email");
        q.setParameter("email", email);
        
        return (User) q.uniqueResult();
        
    }
    
    public String uploadPictureToServer(UploadedFile image, Long pictureId) throws IOException{
        
        String fileName = null;
        
        if(image!=null){
            
            try {
                
                fileName = PATH_PHOTO + pictureId + ".jpg";
                
                IOUtils.copy(image.getFile(), new FileOutputStream(new File(fileName)));
                
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new FileNotFoundException("File wasn't found");
            } catch (IOException e){
                e.printStackTrace();
                throw new IOException("Not possible to send the file");
            }
        }
        
        return fileName;
    }
    
}
