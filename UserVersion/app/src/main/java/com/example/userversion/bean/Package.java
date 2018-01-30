package com.example.userversion.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

/**
 * Created by XJP on 2017/12/3.
 */
public class Package extends DataSupport implements Serializable {

    String name;
    String email1;
    String email2;
    String email3;
    String email4;
    String email5;
    String email6;
    String email7;
    String email8;
    String email9;
    String email10;
    String wifiId;
    String wifiPsw;
    String distance;
    String emailTitle;
    String emailContent;
    String qrCode;

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getEmail3() {
        return email3;
    }

    public void setEmail3(String email3) {
        this.email3 = email3;
    }

    public String getEmail4() {
        return email4;
    }

    public void setEmail4(String email4) {
        this.email4 = email4;
    }

    public String getEmail5() {
        return email5;
    }

    public void setEmail5(String email5) {
        this.email5 = email5;
    }

    public String getEmail6() {
        return email6;
    }

    public void setEmail6(String email6) {
        this.email6 = email6;
    }

    public String getEmail7() {
        return email7;
    }

    public void setEmail7(String email7) {
        this.email7 = email7;
    }

    public String getEmail8() {
        return email8;
    }

    public void setEmail8(String email8) {
        this.email8 = email8;
    }

    public String getEmail9() {
        return email9;
    }

    public void setEmail9(String email9) {
        this.email9 = email9;
    }

    public String getEmail10() {
        return email10;
    }

    public void setEmail10(String email10) {
        this.email10 = email10;
    }

    long time;
    boolean isDelivery=false;
    public long getTime() {
        return time;
    }



    public void setTime(long time) {
        this.time = time;
    }

    public boolean isDelivery() {
        return isDelivery;
    }

    public void setDelivery(boolean delivery) {
        isDelivery = delivery;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getWifiId() {
        return wifiId;
    }

    public void setWifiId(String wifiId) {
        this.wifiId = wifiId;
    }

    public String getWifiPsw() {
        return wifiPsw;
    }

    public void setWifiPsw(String wifiPsw) {
        this.wifiPsw = wifiPsw;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getEmailTitle() {
        return emailTitle;
    }

    public void setEmailTitle(String emailTitle) {
        this.emailTitle = emailTitle;
    }

    public String getEmailContent() {
        return emailContent;
    }

    public void setEmailContent(String emailContent) {
        this.emailContent = emailContent;
    }
}
