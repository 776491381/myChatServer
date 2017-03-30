package cn.fyypumpkin.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by fyy on 3/27/17.
 */
@Entity
@Table(name = "Users", schema = "ChatData", catalog = "")
public class UsersEntity implements Serializable{


    private String username;
    private String passwd;
    private Date regtime;
    private Set<FriendsEntity> friends;
    private Set<ChatHistoryEntity> chatHistory;

    public UsersEntity() {
    }

    public UsersEntity(String username, String passwd, Date regtime, Set<FriendsEntity> friends, Set<ChatHistoryEntity> chatHistory) {
        this.username = username;
        this.passwd = passwd;
        this.regtime = regtime;
        this.friends = friends;
        this.chatHistory = chatHistory;
    }

    public Set<ChatHistoryEntity> getChatHistory() {

        return chatHistory;
    }

    public void setChatHistory(Set<ChatHistoryEntity> chatHistory) {
        this.chatHistory = chatHistory;
    }

    public Set<FriendsEntity> getFriends() {
        return friends;
    }

    public void setFriends(Set<FriendsEntity> friends) {
        this.friends = friends;
    }

    @Id
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "passwd")
    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    @Basic
    @Column(name = "regtime")
    public Date getRegtime() {
        return regtime;
    }

    public void setRegtime(Date regtime) {
        this.regtime = regtime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UsersEntity that = (UsersEntity) o;

        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (passwd != null ? !passwd.equals(that.passwd) : that.passwd != null) return false;
        if (regtime != null ? !regtime.equals(that.regtime) : that.regtime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (passwd != null ? passwd.hashCode() : 0);
        result = 31 * result + (regtime != null ? regtime.hashCode() : 0);
        return result;
    }
}
