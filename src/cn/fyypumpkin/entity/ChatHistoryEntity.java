package cn.fyypumpkin.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by fyy on 3/27/17.
 */
@Entity
@Table(name = "ChatHistory", schema = "ChatData", catalog = "")
public class ChatHistoryEntity {
    private String username;
    private String message;
    private Timestamp msgtime;
    private String friendname;

    public ChatHistoryEntity(String username, String message, Timestamp msgtime, String friendname) {
        this.username = username;
        this.message = message;
        this.msgtime = msgtime;
        this.friendname = friendname;
    }

    public ChatHistoryEntity() {
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
    @Column(name = "message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Basic
    @Column(name = "msgtime")
    public Timestamp getMsgtime() {
        return msgtime;
    }

    public void setMsgtime(Timestamp msgtime) {
        this.msgtime = msgtime;
    }

    @Basic
    @Column(name = "friendname")
    public String getFriendname() {
        return friendname;
    }

    public void setFriendname(String friendname) {
        this.friendname = friendname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatHistoryEntity that = (ChatHistoryEntity) o;

        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        if (msgtime != null ? !msgtime.equals(that.msgtime) : that.msgtime != null) return false;
        if (friendname != null ? !friendname.equals(that.friendname) : that.friendname != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (msgtime != null ? msgtime.hashCode() : 0);
        result = 31 * result + (friendname != null ? friendname.hashCode() : 0);
        return result;
    }
}
