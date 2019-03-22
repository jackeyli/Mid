package li.yifei4.service.course;

import li.yifei4.data.entity.course.Course;
import li.yifei4.data.entity.course.Grade;

import java.util.List;

public interface GradeStrategy {
    List<Grade> gradeCourse(Course course);
}
