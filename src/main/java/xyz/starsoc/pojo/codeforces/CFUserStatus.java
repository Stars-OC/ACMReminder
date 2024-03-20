package xyz.starsoc.pojo.codeforces;

import java.io.Serializable;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.String;
import java.util.List;

public class CFUserStatus implements Serializable {
  private Integer contestId;

  private Integer timeConsumedMillis;

  private Integer relativeTimeSeconds;

  private Problem problem;

  private Integer creationTimeSeconds;

  private Author author;

  private String programmingLanguage;

  private String verdict;

  private String testset;

  private Integer passedTestCount;

  private Integer memoryConsumedBytes;

  private Integer id;

  public Integer getContestId() {
    return this.contestId;
  }

  public void setContestId(Integer contestId) {
    this.contestId = contestId;
  }

  public Integer getTimeConsumedMillis() {
    return this.timeConsumedMillis;
  }

  public void setTimeConsumedMillis(Integer timeConsumedMillis) {
    this.timeConsumedMillis = timeConsumedMillis;
  }

  public Integer getRelativeTimeSeconds() {
    return this.relativeTimeSeconds;
  }

  public void setRelativeTimeSeconds(Integer relativeTimeSeconds) {
    this.relativeTimeSeconds = relativeTimeSeconds;
  }

  public Problem getProblem() {
    return this.problem;
  }

  public void setProblem(Problem problem) {
    this.problem = problem;
  }

  public Integer getCreationTimeSeconds() {
    return this.creationTimeSeconds;
  }

  public void setCreationTimeSeconds(Integer creationTimeSeconds) {
    this.creationTimeSeconds = creationTimeSeconds;
  }

  public Author getAuthor() {
    return this.author;
  }

  public void setAuthor(Author author) {
    this.author = author;
  }

  public String getProgrammingLanguage() {
    return this.programmingLanguage;
  }

  public void setProgrammingLanguage(String programmingLanguage) {
    this.programmingLanguage = programmingLanguage;
  }

  public String getVerdict() {
    return this.verdict;
  }

  public void setVerdict(String verdict) {
    this.verdict = verdict;
  }

  public String getTestset() {
    return this.testset;
  }

  public void setTestset(String testset) {
    this.testset = testset;
  }

  public Integer getPassedTestCount() {
    return this.passedTestCount;
  }

  public void setPassedTestCount(Integer passedTestCount) {
    this.passedTestCount = passedTestCount;
  }

  public Integer getMemoryConsumedBytes() {
    return this.memoryConsumedBytes;
  }

  public void setMemoryConsumedBytes(Integer memoryConsumedBytes) {
    this.memoryConsumedBytes = memoryConsumedBytes;
  }

  public Integer getId() {
    return this.id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public static class Problem implements Serializable {
    private Integer contestId;

    private String name;

    private Integer rating;

    private String index;

    private String type;

    private List<String> tags;

    public Integer getContestId() {
      return this.contestId;
    }

    public void setContestId(Integer contestId) {
      this.contestId = contestId;
    }

    public String getName() {
      return this.name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public Integer getRating() {
      return this.rating;
    }

    public void setRating(Integer rating) {
      this.rating = rating;
    }

    public String getIndex() {
      return this.index;
    }

    public void setIndex(String index) {
      this.index = index;
    }

    public String getType() {
      return this.type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public List<String> getTags() {
      return this.tags;
    }

    public void setTags(List<String> tags) {
      this.tags = tags;
    }
  }

  public static class Author implements Serializable {
    private Integer contestId;

    private Boolean ghost;

    private List<Members> members;

    private String participantType;

    private Integer startTimeSeconds;

    public Integer getContestId() {
      return this.contestId;
    }

    public void setContestId(Integer contestId) {
      this.contestId = contestId;
    }

    public Boolean getGhost() {
      return this.ghost;
    }

    public void setGhost(Boolean ghost) {
      this.ghost = ghost;
    }

    public List<Members> getMembers() {
      return this.members;
    }

    public void setMembers(List<Members> members) {
      this.members = members;
    }

    public String getParticipantType() {
      return this.participantType;
    }

    public void setParticipantType(String participantType) {
      this.participantType = participantType;
    }

    public Integer getStartTimeSeconds() {
      return this.startTimeSeconds;
    }

    public void setStartTimeSeconds(Integer startTimeSeconds) {
      this.startTimeSeconds = startTimeSeconds;
    }

    public static class Members implements Serializable {
      private String handle;

      public String getHandle() {
        return this.handle;
      }

      public void setHandle(String handle) {
        this.handle = handle;
      }
    }
  }
}
