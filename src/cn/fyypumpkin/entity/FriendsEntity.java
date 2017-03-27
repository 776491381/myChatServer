package cn.fyypumpkin.entity;

import javax.persistence.*;

/**
 * Created by fyy on 3/27/17.
 */
@Entity
@Table(name = "Friends", schema = "ChatData", catalog = "")
public class FriendsEntity {
    private String username;
    private String friendname;

    @Id
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

        FriendsEntity that = (FriendsEntity) o;

        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (friendname != null ? !friendname.equals(that.friendname) : that.friendname != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (friendname != null ? friendname.hashCode() : 0);
        return result;
    }
}
