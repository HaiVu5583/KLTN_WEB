/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kltn.controller;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import kltn.dao.ATMLocationDAO;
import kltn.entity.AtmLocation;

/**
 *
 * @author Vu
 */
@ManagedBean(name="atmController")
@ViewScoped
public class AtmLocationController {
    private ATMLocationDAO atmDAO;
    private List<AtmLocation> atmList;
    private AtmLocation selectedAtm;

    @PostConstruct
    public void init(){
        atmDAO = new ATMLocationDAO();
        atmList = atmDAO.listAll();
        
    }
    
    
    public List<AtmLocation> getAtmList() {
        return atmList;
    }

    public void setAtmList(List<AtmLocation> atmList) {
        this.atmList = atmList;
    }

    public AtmLocation getSelectedAtm() {
        return selectedAtm;
    }

    public void setSelectedAtm(AtmLocation selectedAtm) {
        this.selectedAtm = selectedAtm;
    }

    
    
    
}
