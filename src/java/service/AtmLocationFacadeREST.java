/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.List;
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
import kltn.dao.ATMLocationDAO;
import kltn.entity.AtmLocation;

/**
 *
 * @author Vu
 */
@javax.ejb.Stateless
@Path("atm")
public class AtmLocationFacadeREST extends AbstractFacade<AtmLocation> {

    @PersistenceContext(unitName = "KLTN_WEBPU")
    private EntityManager em;
    private ATMLocationDAO atmDAO;

    public AtmLocationFacadeREST() {
        super(AtmLocation.class);
        atmDAO = new ATMLocationDAO();
    }

//    @POST
//    @Override
//    @Consumes({"application/xml", "application/json"})
//    public void create(AtmLocation entity) {
//        super.create(entity);
//    }
//
//    @PUT
//    @Path("{id}")
//    @Consumes({"application/xml", "application/json"})
//    public void edit(@PathParam("id") Integer id, AtmLocation entity) {
//        super.edit(entity);
//    }
//
//    @DELETE
//    @Path("{id}")
//    public void remove(@PathParam("id") Integer id) {
//        super.remove(super.find(id));
//    }
//    @GET
//    @Path("{id}")
//    @Produces({"application/xml", "application/json"})
//    public AtmLocation find(@PathParam("id") Integer id) {
//        return super.find(id);
//    }
    @GET
    @Override
    @Produces("application/json;charset=UTF-8")
    public List<AtmLocation> findAll() {
        return atmDAO.findByGeocodingStatus('1');
    }

    @GET
    @Path("0/{lat}/{long}")
    @Produces("application/json;charset=UTF-8")
    public List<AtmLocation> findNearestAtm(@PathParam("lat") String lat1, @PathParam("long") String long1) {
        return atmDAO.find10NeareastATM(lat1, long1, "", '0');
    }

    @GET
    @Path("{type}/{name}")
    @Produces("application/json;charset=UTF-8")
    public List<AtmLocation> findAtm(@PathParam("type") String type, @PathParam("name") String name) {
//        return atmDAO.find10NeareastATM(lat1, long1, "", '0');
        return atmDAO.listByName(type, name);
    }
    
    @GET
    @Path("{type}/{bank}/{lat}/{long}")
    @Produces("application/json;charset=UTF-8")
    public List<AtmLocation> findNearestAtm(@PathParam("type") String type, @PathParam("bank") String bank, @PathParam("lat") String lat1, @PathParam("long") String long1) {
        if (type.equals("1")) {
            return atmDAO.find10NeareastATM(lat1, long1, bank, '1');
        } else if (type.equals("2")) {
            return atmDAO.find10NeareastATM(lat1, long1, bank, '2');
        }
        return atmDAO.find10NeareastATM(lat1, long1, bank, '3');
    }
    @Override
    protected EntityManager getEntityManager() {
        return em;

    }

}
