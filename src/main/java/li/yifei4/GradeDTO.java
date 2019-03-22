package li.yifei4;

import li.yifei4.data.entity.course.Grade;

public class GradeDTO {
    private String courseName;
    private Grade.GradeValue grade;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Grade.GradeValue getGrade() {
        return grade;
    }

    public void setGrade(Grade.GradeValue grade) {
        this.grade = grade;
    }
}
