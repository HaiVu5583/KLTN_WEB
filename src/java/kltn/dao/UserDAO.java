/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kltn.dao;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import kltn.entity.User;
import kltn.hibernate.HibernateUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
/**
 *
 * @author Vu
 */
public class UserDAO implements Serializable{
    public boolean login(String username, String password) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        String md5 = new BigInteger(DigestUtils.md5(password)).toString(16);
        System.out.println(md5);
        List<String> list = null;
        Transaction tx = null;
        try {

            tx = session.beginTransaction();
            Criteria cr = session.createCriteria(User.class);
            cr.add(Restrictions.eq("username", username));
            cr.add(Restrictions.eq("password", md5));
            list = cr.list();
            System.out.println(list.size());
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
        return (list.size()==1);
    }
    
}
