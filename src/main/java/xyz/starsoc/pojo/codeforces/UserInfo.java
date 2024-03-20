package xyz.starsoc.pojo.codeforces;

import java.io.Serializable;

public class UserInfo implements Serializable {

    private String username;

    private Integer rating;

    private Long updateTime;

    private String lastContestName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getLastContestName() {
        return lastContestName;
    }

    public void setLastContestName(String lastContestName) {
        this.lastContestName = lastContestName;
    }
}
