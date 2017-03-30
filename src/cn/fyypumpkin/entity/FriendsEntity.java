package cn.fyypumpkin.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by fyy on 3/27/17.
 */
@Entity
@Table(name = "Friends", schema = "ChatData", catalog = "")
public class FriendsEntity implements Serializable{

    private String fid;
    private String friendname;

    public FriendsEntity(String fid, String friendname) {
        this.fid = fid;
        this.friendname = friendname;
    }

    public FriendsEntity() {
    }

    @Id
    @Column(name = "fid")
    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
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

        if (fid != null ? !fid.equals(that.fid) : that.fid != null) return false;
        if (friendname != null ? !friendname.equals(that.friendname) : that.friendname != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = fid != null ? fid.hashCode() : 0;
        result = 31 * result + (friendname != null ? friendname.hashCode() : 0);
        return result;
    }
}
