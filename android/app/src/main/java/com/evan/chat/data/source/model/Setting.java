package com.evan.chat.data.source.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Objects;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/1/30
 * Time: 下午6:14
 */
@Entity
public class Setting {

    @Id
    private Long id;
    private String title;
    private String value;
    private String caption;
    private int display;

    public Setting(Setting setting){
        this.id = setting.id;
        this.title = setting.title;
        this.value = setting.value;
        this.display = setting.display;
        this.caption = setting.caption;
    }

    @Generated(hash = 366504907)
    public Setting(Long id, String title, String value, String caption,
            int display) {
        this.id = id;
        this.title = title;
        this.value = value;
        this.caption = caption;
        this.display = display;
    }

    @Generated(hash = 909716735)
    public Setting() {
    }

    @Override
    public String toString() {
        return "Setting{" +
                "title='" + title + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Setting setting = (Setting) o;
        return display == setting.display &&
                Objects.equals(id, setting.id) &&
                Objects.equals(title, setting.title) &&
                Objects.equals(value, setting.value) &&
                Objects.equals(caption, setting.caption);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, title, value, caption, display);
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public int getDisplay() {
        return display;
    }

    public void setDisplay(int display) {
        this.display = display;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
