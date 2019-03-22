package li.yifei4;

import li.yifei4.data.entity.course.Course;
import li.yifei4.data.entity.course.EvaluationRule;
import li.yifei4.data.entity.role.Teacher;

public class CourseDTO {
    private long id;
    private String name;
    private int capacity;
    private long teacherID = -1;
    private long semesterID = -1;
    private String schedule;
    private long registedCount = 0;
    private int credit;
    private String Url;
    private long evaluationRule = -1;
    private String location;
    private String courseType;
    private String semesterName;
    private String teacherName;
    private long version;
    private EvaluationRule rule;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public long getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(long teacherID) {
        this.teacherID = teacherID;
    }

    public long getSemesterID() {
        return semesterID;
    }

    public void setSemesterID(long semesterID) {
        this.semesterID = semesterID;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public long getEvaluationRule() {
        return evaluationRule;
    }

    public void setEvaluationRule(long evaluationRule) {
        this.evaluationRule = evaluationRule;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getSemesterName() {
        return semesterName;
    }

    public void setSemesterName(String semesterName) {
        this.semesterName = semesterName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
    public EvaluationRule getRule() {
        return rule;
    }

    public void setRule(EvaluationRule rule) {
        this.rule = rule;
    }

    public long getRegistedCount() {
        return registedCount;
    }

    public void setRegistedCount(long registedCount) {
        this.registedCount = registedCount;
    }
}
