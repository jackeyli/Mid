package li.yifei4.data.entity.course;

import li.yifei4.data.entity.Entity;
import li.yifei4.data.table.tableManager.DataManager;

import java.util.List;
import java.util.stream.Collectors;

public class AssignmentSubmission extends Entity {
    private long studentId = -1;
    private long assignmentId = -1;
    private String essayContent;
    private String[] multipleChoice;
    public String getEssayContent() {
        return essayContent;
    }
    public Assignment retrieveAssignment(){
        return (Assignment) Entity.findById(Assignment.class,assignmentId);
    }
    public static List<AssignmentSubmission> findByStudentNAssignment(long studentId, long assignmentId){
        return (List<AssignmentSubmission>) DataManager.getTable(AssignmentSubmission.class.getSimpleName())
                .getTableData().stream().filter((t)->
                        ((AssignmentSubmission)t).getStudentId() == studentId &&
                        ((AssignmentSubmission)t).getAssignmentId() == assignmentId
                ).collect(Collectors.toList());
    }
    public static List<AssignmentSubmission> findByAssignment(long assignmentId){
        return (List<AssignmentSubmission>) DataManager.getTable(AssignmentSubmission.class.getSimpleName())
                .getTableData().stream().filter((t)->((AssignmentSubmission)t).getAssignmentId() == assignmentId).collect(Collectors.toList());
    }
    public void setEssayContent(String essayContent) {
        this.essayContent = essayContent;
    }

    public String[] getMultipleChoice() {
        return multipleChoice;
    }

    public void setMultipleChoice(String[] multipleChoice) {
        this.multipleChoice = multipleChoice;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public long getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(long assignmentId) {
        this.assignmentId = assignmentId;
    }
}
