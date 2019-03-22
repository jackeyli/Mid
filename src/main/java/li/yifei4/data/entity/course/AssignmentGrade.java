package li.yifei4.data.entity.course;

import li.yifei4.data.entity.Entity;
import li.yifei4.data.table.tableManager.DataManager;

import java.util.Optional;

public class AssignmentGrade extends Entity {

    private int score;
    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public long getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(long assignmentId) {
        this.assignmentId = assignmentId;
    }
    private long studentId = -1;
    private long assignmentId = -1;
    public static AssignmentGrade findByAssignmentSubmission(long assignmentSubId){
        AssignmentSubmission assnsub = (AssignmentSubmission) Entity.findById(AssignmentSubmission.class,assignmentSubId);
        return (AssignmentGrade) DataManager.getTable(AssignmentGrade.class.getSimpleName()).getTableData()
                .stream().filter((t)->((AssignmentGrade)t).getAssignmentId() == Optional.ofNullable(assnsub).map((p)->p.getAssignmentId())
                        .orElse((long)-1) && ((AssignmentGrade)t).getStudentId() == assnsub.getStudentId()).findAny().orElse(null);
    }
    public static AssignmentGrade findByStudentAndAssignment(long assignmentId,long studentId){
        return (AssignmentGrade) DataManager.getTable(AssignmentGrade.class.getSimpleName()).getTableData()
                .stream().filter((t)->((AssignmentGrade)t).getAssignmentId() == assignmentId
                        && ((AssignmentGrade)t).getStudentId() == studentId).findAny().orElse(null);
    }
}
