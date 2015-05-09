/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import kltn.dao.BankInfoDAO;
import kltn.entity.BankInfo;

/**
 *
 * @author Vu
 */
@Stateless
@Path("bankinfo")
public class BankInfoFacadeREST extends AbstractFacade<BankInfo> {
    @PersistenceContext(unitName = "KLTN_WEBPU")
    private EntityManager em;
    private BankInfoDAO bankInfoDAO;
    public BankInfoFacadeREST() {
        super(BankInfo.class);
        bankInfoDAO = new BankInfoDAO();
    }

   
    @GET
    @Override
    @Produces("application/json;charset=UTF-8")
    public List<BankInfo> findAll() {
        return bankInfoDAO.listAll();
    }
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
