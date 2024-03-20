package xyz.starsoc.pojo.codeforces;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.String;

public class CFUserRating implements Serializable {
  private Integer oldRating;

  private Integer contestId;

  private Long ratingUpdateTimeSeconds;

  private Integer newRating;

  private Integer rank;

  private String handle;

  private String contestName;

  public Integer getOldRating() {
    return this.oldRating;
  }

  public void setOldRating(Integer oldRating) {
    this.oldRating = oldRating;
  }

  public Integer getContestId() {
    return this.contestId;
  }

  public void setContestId(Integer contestId) {
    this.contestId = contestId;
  }

  public Long getRatingUpdateTimeSeconds() {
    return this.ratingUpdateTimeSeconds;
  }

  public void setRatingUpdateTimeSeconds(Long ratingUpdateTimeSeconds) {
    this.ratingUpdateTimeSeconds = ratingUpdateTimeSeconds;
  }

  public Integer getNewRating() {
    return this.newRating;
  }

  public void setNewRating(Integer newRating) {
    this.newRating = newRating;
  }

  public Integer getRank() {
    return this.rank;
  }

  public void setRank(Integer rank) {
    this.rank = rank;
  }

  public String getHandle() {
    return this.handle;
  }

  public void setHandle(String handle) {
    this.handle = handle;
  }

  public String getContestName() {
    return this.contestName;
  }

  public void setContestName(String contestName) {
    this.contestName = contestName;
  }
}
