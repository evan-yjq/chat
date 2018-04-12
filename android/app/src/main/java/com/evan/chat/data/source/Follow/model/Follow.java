package com.evan.chat.data.source.Follow.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Objects;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by IntelliJ IDEA
 * User: Evan
 * Date: 2018/4/1
 * Time: 16:19
 */
@Entity
public class Follow {
    @Id
    private Long id;
    private String account;
    private String nickname;
    private String email;
    private String relationship;
    private String classification;
    private String profile;

    public Follow(Long id, String account, String nickname, String relationship, String classification) {
        this(id,account,nickname,null,relationship,classification,null);
    }

    @Generated(hash = 1528662446)
    public Follow(Long id, String account, String nickname, String email, String relationship,
                  String classification, String profile) {
        this.id = id;
        this.account = account;
        this.nickname = nickname;
        this.email = email;
        this.relationship = relationship;
        this.classification = classification;
        this.profile = profile;
    }

    @Generated(hash = 287143722)
    public Follow() {
    }

    @Override
    public String toString() {
        return "Follow{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", relationship='" + relationship + '\'' +
                ", classification='" + classification + '\'' +
                ", profile='" + profile + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Follow follow = (Follow) o;
        return Objects.equals(id, follow.id) &&
                Objects.equals(account, follow.account) &&
                Objects.equals(nickname, follow.nickname) &&
                Objects.equals(email, follow.email) &&
                Objects.equals(relationship, follow.relationship) &&
                Objects.equals(classification, follow.classification) &&
                Objects.equals(profile, follow.profile);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, account, nickname, email, relationship, classification, profile);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}