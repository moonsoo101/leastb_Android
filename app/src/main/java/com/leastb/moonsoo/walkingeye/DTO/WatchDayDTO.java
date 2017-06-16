package com.leastb.moonsoo.walkingeye.DTO;


public class WatchDayDTO {
    private int id;
    private String date, count;

    public WatchDayDTO(int id, String date, String count) {
        this.id = id;
        this.date = date;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
