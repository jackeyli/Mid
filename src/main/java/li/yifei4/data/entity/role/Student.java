package li.yifei4.data.entity.role;

import li.yifei4.CourseDTO;
import li.yifei4.GradeDTO;
import li.yifei4.data.entity.Entity;
import li.yifei4.data.entity.course.*;
import li.yifei4.data.entity.scholarShip.BalanceCalculationDTO;
import li.yifei4.data.entity.scholarShip.CreditRate;
import li.yifei4.data.entity.scholarShip.ScholarShip;
import li.yifei4.data.entity.scholarShip.ScholarShipAssignment;
import li.yifei4.data.table.DataTable;
import li.yifei4.data.table.tableManager.DataManager;
import li.yifei4.service.course.Assignmentable;

import java.util.*;
import java.util.stream.Collectors;

public class Student extends User implements Assignmentable {
    private String userType = User.USER_TYPE_STUDENT;
    public Student(){

    }
    public Student(String name, String password) {
        super(name, password);
    }
    public List<Assignment> browseAssgnment(long courseId){
        return Assignment.findByCourse(courseId);
    }

    @Override
    public void gradeAssignment(long assnSubId,int score) {
        throw new RuntimeException("You cannot do this");
    }

    @Override
    public void createAssignment(Assignment assn) {
        throw new RuntimeException("You cannot do this");
    }

    @Override
    public void updateAssignment(Assignment assn) {
        throw new RuntimeException("You cannot do this");
    }

    @Override
    public List<Course> retrieveRelatedCourse(String semesterName) {
       return (List<Course>) Course.findBySemester(semesterName).stream().filter((t)->
                CourseRegDocument.findByCourseAndStudent(((CourseDTO)t).getId(),this.getId()) != null
        ).collect(Collectors.toList());
    }

    public DataTable retrieveRelatedTable(){
        return DataManager.getTable(User.class.getSimpleName());
    }
    public List<Course> browseMyCourseBySemester(Semester semester){
        return (List<Course>) DataManager.getTable(CourseRegDocument.class.getSimpleName())
                .getTableData().parallelStream().filter((t)->((CourseRegDocument)t).getStudentId() == this.getId())
                .map((t)->Entity.findById(Course.class,((CourseRegDocument) t).getCourseId()))
                .filter((t)->Optional.ofNullable(semester).map((p) -> p.getId()).orElse((long)-2) ==
                           ((Course)t).getSemesterID())
                .collect(Collectors.toList());
    }
    public void signUpCourse(Course course){
        List<CourseRegDocument> docs = CourseRegDocument.findByCourse(course.getId());
        List<CourseRegDocument> myDocs = CourseRegDocument.findStudentSemester(course.getSemesterID(),this.getId());
        if(docs.stream().anyMatch((t)->t.getStudentId() == this.getId())){
            throw new RuntimeException("The Course has already been signed");
        }
        if(myDocs.stream().mapToInt((t)->{return ((Course)Entity.findById(Course.class,t.getCourseId())).getCredit();}).sum()
                + course.getCredit() > 20){
            throw new RuntimeException("You have exceeded the limit");
        }
        if(course.getCapacity() <= docs.size()){
            throw new RuntimeException("The Course has already full.Please try another course");
        }
        CourseRegDocument nRegDoc = new CourseRegDocument();
        nRegDoc.setStudentId(this.getId());
        nRegDoc.setCourseId(course.getId());
        nRegDoc.insert();
    }
    public void dropCourse(Course course){
        CourseRegDocument doc = CourseRegDocument.findByCourseAndStudent(course.getId(),this.getId());
        List<CourseRegDocument> myDocs = CourseRegDocument.findStudentSemester(course.getSemesterID(),this.getId());
        if(doc == null){
            throw new RuntimeException("The Course hasn't been signed");
        }
        if(myDocs.stream().mapToInt((t)->{return ((Course)Entity.findById(Course.class,t.getCourseId())).getCredit();}).sum()
                - course.getCredit() < 16){
            throw new RuntimeException("You have exceeded the limit");
        }
        doc.delete();
    }
    public List<GradeDTO> viewTranscript(String semesterName){
        return  this.browseMyCourseBySemester(Semester.findByName(semesterName)).stream()
                .map((t)-> Grade.findByStudentAndCourse(this.getId(),t.getId()))
                .map((t)->{
                    GradeDTO ret = new GradeDTO();
                    if(t == null)
                        return null;
                    ret.setCourseName(Optional.ofNullable((Course)Entity.findById(Course.class,((Grade)t).getCourseId()))
                            .map((p)->p.getName()).orElse(null));
                    ret.setGrade(((Grade)t).getGrade());
                    return ret;
                })
                .collect(Collectors.toList());
    }
    public List<AssignmentSubmission> browseAssignmentSubmission(long assignmentId){
        return AssignmentSubmission.findByStudentNAssignment(this.getId(),assignmentId);
    }
    public void createAssignmentSubmission(long assnId,String content){
        Assignment assn = (Assignment) Entity.findById(Assignment.class,assnId);
        AssignmentSubmission assnsubmit = new AssignmentSubmission();
        assnsubmit.setAssignmentId(assnId);
        assnsubmit.setStudentId(this.getId());
        if(assn.getAssignType() == Assignment.AssignmentType.ESSAY){
            assnsubmit.setEssayContent(content);
        } else {
            assnsubmit.setMultipleChoice(content.split(";"));
        }
        assnsubmit.insert();
    }

    public void updateAssignmentSubmission(long assnSubId,String content){
        AssignmentSubmission assnsubmit = (AssignmentSubmission) Entity.findById(AssignmentSubmission.class,assnSubId);
        if(assnsubmit.getStudentId() != this.getId()){
            throw new RuntimeException("You can only modify your own submission");
        }
        Assignment assn = (Assignment) Entity.findById(Assignment.class,assnsubmit.getAssignmentId());
        if(assn.getAssignType() == Assignment.AssignmentType.ESSAY){
            assnsubmit.setEssayContent(content);
        } else {
            assnsubmit.setMultipleChoice(content.split(";"));
        }
        assnsubmit.update();
    }
    public void deleteAssignmentSubmission(long assnSubId){
        AssignmentSubmission assnsubmit = (AssignmentSubmission) Entity.findById(AssignmentSubmission.class,assnSubId);
        if(assnsubmit.getStudentId() != this.getId()){
            throw new RuntimeException("You can only modify your own submission");
        }
        assnsubmit.delete();
    }

    @Override
    public void deleteAssignment(Assignment assn) {
        throw new RuntimeException("You cannot do this");
    }

    public String getUserType() {
        return userType;
    }
    public double calculateMyBalance(String semesterName){
        Semester thisSemester = Semester.findByName(semesterName);
        double rate = (double) Optional.ofNullable(CreditRate.findCreditRate(semesterName))
                .map((t)->t.getPriceCredit()).orElseThrow(()->new RuntimeException("No Credit Rate!"));
        double totalCost = CourseRegDocument.findStudentSemester(thisSemester.getId(),this.getId()).stream()
                .mapToDouble((t)->
                        ((double)((Course)Entity.findById(Course.class,t.getCourseId()))
                            .getCredit()) * rate
                ).sum();
        List<ScholarShip> scholarShips = ScholarShip.findByStudent(this.getId());
        BalanceCalculationDTO dto = new BalanceCalculationDTO();
        dto.setStudent(this);
        dto.setBalance(totalCost);
        scholarShips.stream().forEach((t)->t.calculateBalance(dto));
        return dto.getBalance();
    }
    public void setUserType(String userType) {
        this.userType = userType;
    }
}
