package xyz.starsoc.pojo.codeforces;

import java.io.Serializable;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.String;

public class CFContests implements Serializable {
  private String phase;

  private Integer relativeTimeSeconds;

  private Integer durationSeconds;

  private String name;

  private Boolean frozen;

  private Integer id;

  private String type;

  private Long startTimeSeconds;

  private Boolean started = false;

  public String getPhase() {
    return this.phase;
  }

  public void setPhase(String phase) {
    this.phase = phase;
  }

  public Integer getRelativeTimeSeconds() {
    return this.relativeTimeSeconds;
  }

  public void setRelativeTimeSeconds(Integer relativeTimeSeconds) {
    this.relativeTimeSeconds = relativeTimeSeconds;
  }

  public Integer getDurationSeconds() {
    return this.durationSeconds;
  }

  public void setDurationSeconds(Integer durationSeconds) {
    this.durationSeconds = durationSeconds;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Boolean getFrozen() {
    return this.frozen;
  }

  public void setFrozen(Boolean frozen) {
    this.frozen = frozen;
  }

  public Integer getId() {
    return this.id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Long getStartTimeSeconds() {
    return this.startTimeSeconds;
  }

  public void setStartTimeSeconds(Long startTimeSeconds) {
    this.startTimeSeconds = startTimeSeconds;
  }

  public Boolean getStarted() {
    return started;
  }

  public void setStarted(Boolean started) {
    this.started = started;
  }
}
