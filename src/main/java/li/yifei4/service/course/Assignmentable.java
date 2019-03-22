package li.yifei4.service.course;

import li.yifei4.data.entity.course.Assignment;
import li.yifei4.data.entity.course.AssignmentSubmission;
import li.yifei4.data.entity.course.Course;

import java.util.List;

public interface Assignmentable {
    List<AssignmentSubmission> browseAssignmentSubmission(long assignmentId);
    void createAssignmentSubmission(long assnId,String content);
    void updateAssignmentSubmission(long assnSubId,String content);
    void deleteAssignmentSubmission(long assnSubId);
    void deleteAssignment(Assignment assn);
    List<Assignment> browseAssgnment(long courseId);
    void gradeAssignment(long assnSubId,int score);
    void createAssignment(Assignment assn);
    void updateAssignment(Assignment assn);
    List<Course> retrieveRelatedCourse(String semesterName);
}
