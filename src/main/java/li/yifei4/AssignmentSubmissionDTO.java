package li.yifei4;

public class AssignmentSubmissionDTO {
    private long id;
    private long studentId = -1;
    private long assignmentId = -1;
    private String content;
    private int point;
    private boolean graded = false;
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
    public boolean isGraded() {
        return graded;
    }

    public void setGraded(boolean graded) {
        this.graded = graded;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
