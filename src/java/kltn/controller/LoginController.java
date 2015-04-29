/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kltn.controller;

import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import kltn.dao.UserDAO;

/**
 *
 * @author Vu
 */
@ManagedBean(name = "loginController")
@ViewScoped
public class LoginController implements Serializable {

    private String username;
    private String password;
    private UserDAO userDAO;

    @PostConstruct
    public void init() {
        userDAO = new UserDAO();
    }

    public void login() throws IOException {
        if (!userDAO.login(username, password)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Tên đăng nhập hoặc mật khẩu không đúng"));
        } else {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.getSessionMap().put("user", username);
            ec.redirect(ec.getRequestContextPath()+"/faces/index.xhtml");
        }
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
