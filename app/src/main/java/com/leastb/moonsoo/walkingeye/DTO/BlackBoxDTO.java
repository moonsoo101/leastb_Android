package com.leastb.moonsoo.walkingeye.DTO;

/**
 * Created by wisebody on 2017. 6. 7..
 */

public class BlackBoxDTO {
    private String imgName;
    private int isAccident;
    private String address;

    public BlackBoxDTO(String imgName, int isAccident, String address) {
        this.imgName = imgName;
        this.isAccident = isAccident;
        this.address = address;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public int getIsAccident() {
        return isAccident;
    }

    public void setIsAccident(int isAccident) {
        this.isAccident = isAccident;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
