package li.yifei4;

public class ScholarShipAssignmentDTO {
    private long studentId = -1;
    private long scholarShipId = -1;
    private String scholarShipName;
    private String studentName;
    private long id = -1;
    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public long getScholarShipId() {
        return scholarShipId;
    }

    public void setScholarShipId(long scholarShipId) {
        this.scholarShipId = scholarShipId;
    }

    public String getScholarShipName() {
        return scholarShipName;
    }

    public void setScholarShipName(String scholarShipName) {
        this.scholarShipName = scholarShipName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
