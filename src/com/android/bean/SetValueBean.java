package com.android.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2016/12/14.
 */
@Table(name = "info")
public class SetValueBean {
    @Column(name = "id", isId = true)
    private int id;
    @Column(name = "qinjiao")
    private double qinjiao;
    @Column(name = "fangwei")
    private double fangwei;
    @Column(name = "kongshen")
    private double kongshen;
    @Column(name = "shang")
    private double shang;
    @Column(name = "zuo")
    private double zuo;
    @Column(name = "zong")
    private double zong;
    @Column(name = "fileName")
    private String fileName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getQinjiao() {
        return qinjiao;
    }

    public void setQinjiao(double qinjiao) {
        this.qinjiao = qinjiao;
    }

    public double getFangwei() {
        return fangwei;
    }

    public void setFangwei(double fangwei) {
        this.fangwei = fangwei;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String file) {
        this.fileName = file;
    }

    public double getZuo() {
        return zuo;
    }

    public void setZuo(double zuo) {
        this.zuo = zuo;
    }

    public double getKongshen() {
        return kongshen;
    }

    public void setKongshen(double kongshen) {
        this.kongshen = kongshen;
    }

    public double getShang() {
        return shang;
    }

    public void setShang(double shang) {
        this.shang = shang;
    }

    public double getZong() {
        return zong;
    }

    public void setZong(double zong) {
        this.zong = zong;
    }
}
