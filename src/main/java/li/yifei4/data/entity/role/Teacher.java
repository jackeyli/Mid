package li.yifei4.data.entity.role;

import li.yifei4.CourseDTO;
import li.yifei4.data.entity.Entity;
import li.yifei4.data.entity.course.*;
import li.yifei4.data.table.DataTable;
import li.yifei4.data.table.tableManager.DataManager;
import li.yifei4.service.course.Assignmentable;
import li.yifei4.service.course.EvaluationRuleFactory;

import java.util.List;
import java.util.stream.Collectors;

public class Teacher extends User implements Assignmentable {
    private String userType = User.USER_TYPE_TEACHER;

    public Teacher(){

    }
    public Teacher(String name, String password) {
        super(name, password);
    }
    @Override
    public List<Course> retrieveRelatedCourse(String semesterName) {
        return (List<Course>) Course.findBySemester(semesterName).stream().filter((t)->
                ((CourseDTO)t).getTeacherID() == this.getId()
        ).collect(Collectors.toList());
    }
    public static List<Teacher> findAll(){
        return (List<Teacher>) DataManager.getTable(User.class.getSimpleName())
                .getTableData().stream().filter((t)->{
                    return t instanceof Teacher;
                }).collect(Collectors.toList());
    }
    public void createAssignment(Assignment assn){
        assn.insert();
    }

    @Override
    public void updateAssignment(Assignment assn) {
        assn.update();
    }

    public void deleteAssignment(Assignment assn){
        assn.delete();
        AssignmentSubmission.findByAssignment(assn.getId()).stream()
                .forEach((t)->{
                    AssignmentGrade.findByAssignmentSubmission(t.getId()).delete();
                    t.delete();
                });
    }
    public List<Assignment> browseAssgnment(long courseId){
        return Assignment.findByCourse(courseId);
    }
    public List<AssignmentSubmission> browseAssignmentSubmission(long assignmentId){
        return AssignmentSubmission.findByAssignment(assignmentId);
    }

    @Override
    public void createAssignmentSubmission(long assnId, String content) {
        throw new RuntimeException("You cannot do this");
    }

    @Override
    public void updateAssignmentSubmission(long assnSubId, String content) {
        throw new RuntimeException("You cannot do this");
    }

    @Override
    public void deleteAssignmentSubmission(long assnSubId) {
        throw new RuntimeException("You cannot do this");
    }
    public DataTable retrieveRelatedTable(){
        return DataManager.getTable(User.class.getSimpleName());
    }
    public void gradeAssignment(long assignSubId,int score){
        AssignmentSubmission assnsub = (AssignmentSubmission) Entity.findById(AssignmentSubmission.class,assignSubId);
        AssignmentGrade grade = AssignmentGrade.findByAssignmentSubmission(assignSubId);
        boolean isNew = false;
        if(grade == null){
            grade = new AssignmentGrade();
            isNew = true;
        }
        grade.setAssignmentId(assnsub.getAssignmentId());
        grade.setStudentId(assnsub.getStudentId());
        grade.setScore(score);
        if(isNew)
            grade.insert();
        else
            grade.update();
    }

    public void createEvaluationRule(Course course, String type,double aValue,double bValue){
        EvaluationRule rule = EvaluationRuleFactory.getEvaluationRule(type,aValue,bValue);
        rule.insert();
        course.setEvaluationRule(rule.getId());
        course.update();
    }
    public void gradeCourse(long courseId){
        Course course = (Course) Entity.findById(Course.class,courseId);
        EvaluationRule rule = course.retrieveEvaluationRule();
        if(rule == null)
            throw new RuntimeException("The Evaluation Rule hasn't been set yet");
        rule.gradeCourse(course).forEach((t)->t.insert());
    }
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
