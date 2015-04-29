/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kltn.controller;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Vu
 */
@ManagedBean(name = "startController")
@ViewScoped
public class StartController {

    private String status;
    private boolean renderProgress;

    @PostConstruct
    public void init() {
        renderProgress = false;
    }

    public void crawlingData() {
        renderProgress = true;
        status = "Đang tiến hành thu thập dữ liệu ...";
        RequestContext.getCurrentInstance().execute("PF('crawlBt').disable()");
    }

    public void standardlize() {
        renderProgress = true;
        status = "Đang tiến hành chuẩn hóa ...";
        RequestContext.getCurrentInstance().execute("PF('standardBt').disable()");
    }

    public void getLatLong() {
        renderProgress = true;
        status = "Đang tiến hành chuyển đổi sang tọa độ địa lí ...";
        RequestContext.getCurrentInstance().execute("PF('latLongBt').disable()");
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isRenderProgress() {
        return renderProgress;
    }

    public void setRenderProgress(boolean renderProgress) {
        this.renderProgress = renderProgress;
    }

}
