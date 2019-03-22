package li.yifei4.data.entity.course;

import li.yifei4.CourseDTO;
import li.yifei4.data.entity.Entity;
import li.yifei4.data.entity.role.Teacher;
import li.yifei4.data.entity.role.User;
import li.yifei4.data.table.tableManager.DataManager;
import li.yifei4.service.course.GradeStrategy;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class Course extends Entity {
    public static final String COURSE_TYPE_OFFLINE = "OFFLINE";
    public static final String COURSE_TYPE_ONLINE = "ONLINE";
    private String name;
    private int capacity;
    private long teacherID = -1;
    private long semesterID = -1;
    private String schedule;
    private int credit;
    private String url;
    private long evaluationRule = -1;
    private String location;
    private String courseType;
    public static List findBySemester(String semester){
        Semester theSemester = Semester.findByName(semester);
        return (List) (DataManager.getTable(Course.class.getSimpleName())
                .getTableData()).stream().filter((t)-> Objects.equals(((Course)t).getSemesterID(),
                        Optional.ofNullable(theSemester)
                                .map(p->p.getId())
                                .orElse((long) -2)))
                .map((t)->{
                    CourseDTO ret = new CourseDTO();
                    BeanUtils.copyProperties(t,ret);
                    ret.setSemesterName(semester);
                    ret.setRule(((Course) t).retrieveEvaluationRule());
                    ret.setTeacherName(
                            Optional.ofNullable((Teacher)Entity.findById(User.class,((Course) t).getTeacherID())).map((p)->p.getName())
                                    .orElse(null));
                    ret.setRegistedCount(CourseRegDocument.findByCourse(((Course)t).getId()).size());
                    return ret;
                })
                .collect(Collectors.toList());
    }
    public static List<Course> findByTeacher(long teacherID){
        return ((List<Course>) DataManager.getTable(Course.class.getSimpleName())
                .getTableData()).stream().filter((t)-> Objects.equals
                (((Course)t).getTeacherID(),teacherID)).collect(Collectors.toList());
    }
    public boolean isValid(){
        return (COURSE_TYPE_ONLINE.equals(this.getCourseType()) &&
                !StringUtils.isEmpty(this.getUrl())) || (
                        COURSE_TYPE_OFFLINE.equals(this.getCourseType())
                 && !StringUtils.isEmpty(this.getLocation())
                );
    }
    @Override
    public void insert(){
        if(!isValid()){
            throw new RuntimeException("Please add Url or Location");
        }
        super.insert();
    }
    @Override
    public void update(){
        if(!isValid()){
            throw new RuntimeException("Please add Url or Location");
        }
        super.update();
    }
    public EvaluationRule retrieveEvaluationRule(){
        return (EvaluationRule)Entity.findById(EvaluationRule.class,this.getEvaluationRule());
    }
    public Teacher retrieveTeacher(){
        return (Teacher)Entity.findById(User.class,this.getTeacherID());
    }
    public Semester retrieveSemester(){
        return (Semester)Entity.findById(Semester.class,this.getSemesterID());
    }
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

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
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

    public long getSemesterID() {
        return semesterID;
    }

    public void setSemesterID(long semesterID) {
        this.semesterID = semesterID;
    }

    public long getEvaluationRule() {
        return evaluationRule;
    }

    public void setEvaluationRule(long evaluationRule) {
        this.evaluationRule = evaluationRule;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
