package li.yifei4.data.entity.course;

import li.yifei4.data.entity.Entity;
import li.yifei4.data.table.tableManager.DataManager;

public class Grade extends Entity {
    public GradeValue getGrade() {
        return grade;
    }

    public void setGrade(GradeValue grade) {
        this.grade = grade;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public enum GradeValue{
        A,B,C
    }
    private long studentId = -1;
    private long courseId = -1;
    private GradeValue grade;

    public Course retrieveCourse(){
        return (Course) Entity.findById(Course.class,courseId);
    }
    public static Grade findByStudentAndCourse(long studentId,long courseId){
        return (Grade) DataManager.getTable(Grade.class.getSimpleName()).getTableData()
                .stream().filter((t)->((Grade)t).getCourseId() == courseId &&
                ((Grade)t).getStudentId() == studentId).findAny().orElse(null);
    }
    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }
}
