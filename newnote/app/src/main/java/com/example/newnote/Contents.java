package com.example.newnote;

import java.io.Serializable;

public class Contents implements Serializable {
    //    "CREATE TABLE tbl_content(_id INTEGER primary key autoincrement,"
//            + "title text, content text, upt_date text, use_pw text)";
    private int _id;
    private String title;
    private String content;
    private String upt_date;
    private String use_pw;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUpt_date() {
        return upt_date;
    }

    public void setUpt_date(String upt_date) {
        this.upt_date = upt_date;
    }

    public String getUse_pw() {
        return use_pw;
    }

    public void setUse_pw(String use_pw) {
        this.use_pw = use_pw;
    }
}
