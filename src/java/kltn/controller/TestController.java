/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kltn.controller;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import kltn.dao.ATMLocationDAO;
import kltn.dao.AreaDAO;
import kltn.entity.Area;
import kltn.entity.AtmLocation;

/**
 *
 * @author Vu
 */
@ManagedBean(name = "testController")
@RequestScoped
public class TestController {
    private AreaDAO areaDAO;

    private List<Area> provinceList;
    private String selectedProvince;

    private List<Area> districtList;
    private String selectedDistrict;

    private List<Area> precinctList;
    private String selectedPrecinct;
    
    @PostConstruct
    public void init(){
        areaDAO = new AreaDAO();
        provinceList = areaDAO.findByType('1');
//        Area province = null;
//        for (Area a:provinceList)
//            if(a.getProvince().toLowerCase().contains("nội"))
//                province = a;
        districtList = areaDAO.findDistrictByProvince("hà nội");
        
    }

    public void onProvinceChange(){
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Province Change"));
//        districtList = areaDAO.findDistrictByProvince(selectedProvince);
    }
    public void onDistrictChange(){
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("DISTRICT Change"));
        precinctList = areaDAO.findPrecinctByProvinceAndDistrict(selectedProvince, selectedDistrict);
    }
    
    
    public List<Area> getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(List<Area> provinceList) {
        this.provinceList = provinceList;
    }

    public String getSelectedProvince() {
        return selectedProvince;
    }

    public void setSelectedProvince(String selectedProvince) {
        this.selectedProvince = selectedProvince;
    }

    public List<Area> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(List<Area> districtList) {
        this.districtList = districtList;
    }

    public String getSelectedDistrict() {
        return selectedDistrict;
    }

    public void setSelectedDistrict(String selectedDistrict) {
        this.selectedDistrict = selectedDistrict;
    }

    public List<Area> getPrecinctList() {
        return precinctList;
    }

    public void setPrecinctList(List<Area> precinctList) {
        this.precinctList = precinctList;
    }

    public String getSelectedPrecinct() {
        return selectedPrecinct;
    }

    public void setSelectedPrecinct(String selectedPrecinct) {
        this.selectedPrecinct = selectedPrecinct;
    }
    
    
}
