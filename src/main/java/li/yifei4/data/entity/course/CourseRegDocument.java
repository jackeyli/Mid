package li.yifei4.data.entity.course;

import li.yifei4.data.entity.Entity;
import li.yifei4.data.entity.role.Student;
import li.yifei4.data.table.tableManager.DataManager;

import java.util.List;
import java.util.stream.Collectors;

public class CourseRegDocument extends Entity {
    private long courseId = -1;
    private long studentId = -1;
    private boolean active = true;
    public long getCourseId() {
        return courseId;
    }
    public Course retrieveCourse(){
        return (Course) Entity.findById(Course.class,courseId);
    }

    public Student getStudent(){
        return (Student) Entity.findById(Student.class,studentId);
    }
    public static CourseRegDocument findByCourseAndStudent(long courseId,long studentId){
        return (CourseRegDocument) DataManager.getTable(CourseRegDocument.class.getSimpleName())
                .getTableData().stream().filter((t)->((CourseRegDocument)t).getCourseId() == courseId &&
                        ((CourseRegDocument)t).getStudentId() == studentId).findAny().orElse(null);
    }
    public static List<CourseRegDocument> findByCourse(long courseId){
        return (List<CourseRegDocument>) DataManager.getTable(CourseRegDocument.class.getSimpleName())
                .getTableData().stream().filter((t)->((CourseRegDocument)t).getCourseId() == courseId)
                .collect(Collectors.toList());
    }
    public static List<CourseRegDocument> findStudentSemester(long semesterID,long studentId){
        return (List<CourseRegDocument>) DataManager.getTable(CourseRegDocument.class.getSimpleName())
                .getTableData().stream().filter((t)-> {
                    return ((CourseRegDocument) t).getStudentId() == studentId;
                }).filter((t)->{
                    Course crs = ((CourseRegDocument)t).retrieveCourse();
                    return semesterID == crs.getSemesterID();
                }).collect(Collectors.toList());
    }
    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
