/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kltn.dao;

import java.util.ArrayList;
import java.util.List;
import kltn.entity.Area;
import kltn.entity.BankInfo;
import kltn.hibernate.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Vu
 */
public class BankInfoDAO {
    public List<BankInfo> listAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<BankInfo> list = null;
        Transaction tx = null;
        try {

            tx = session.beginTransaction();
            Criteria cr = session.createCriteria(BankInfo.class);
            list = cr.list();
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        }
        return list;
    }
    public List<String> findByGroup(String group){
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<BankInfo> list = null;
        List<String> result = new ArrayList<>();
        Transaction tx = null;
        try {

            tx = session.beginTransaction();
            Criteria cr = session.createCriteria(BankInfo.class);
            cr.add(Restrictions.eq("group", group).ignoreCase());
            list = cr.list();
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        }
        for (BankInfo bi:list){
            result.add(bi.getCode());
        }
        return result;
    }
}
