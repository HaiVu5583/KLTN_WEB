/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kltn.controller;

import java.io.IOException;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author Vu
 */
@ManagedBean(name="pageNavigator")
@ViewScoped
public class PageNavigation implements Serializable{
    public void toLoginPage() throws IOException{
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.getSessionMap().remove("user");
        ec.redirect(ec.getRequestContextPath()+"/login");
    }
    public void toManagePage() throws IOException{
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath()+"/manage");
    }
    public void toCrawlPage() throws IOException{
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath()+"/crawl");
    }
}
