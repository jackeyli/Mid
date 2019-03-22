package li.yifei4.tester;

import li.yifei4.ResponseDTO;
import li.yifei4.controllers.AssignmentController;
import li.yifei4.controllers.CourseController;
import li.yifei4.controllers.LoginController;
import li.yifei4.controllers.ScholarShipController;
import li.yifei4.data.entity.course.Assignment;
import li.yifei4.data.entity.course.Course;
import li.yifei4.data.entity.role.CourseManager;
import li.yifei4.data.entity.role.User;
import li.yifei4.data.entity.scholarShip.ScholarShip;

public class MainClass {
    public void initUsers() {
        CommandDTO dto = new CommandDTO(null, LoginController.class,
                "Regist",new Object[]{"Student1","password1",User.USER_TYPE_STUDENT},
                new Class[]{String.class,String.class,String.class});
        Thread t = new Thread(new CommandRunner(dto));
        t.start();
        Thread t2 = new Thread(new CommandRunner(new
                CommandDTO(null, LoginController.class,
                "Regist",new Object[]{"Teacher1","password1",User.USER_TYPE_TEACHER},
                new Class[]{String.class,String.class,String.class})));
        t2.start();
        Thread t3 = new Thread(new CommandRunner(new
                CommandDTO(null, LoginController.class,
                "Regist",new Object[]{"CourseManager1","password1",User.USER_TYPE_COURSEMANAGER},
                new Class[]{String.class,String.class,String.class})));
        t3.start();
        Thread t4 = new Thread(new CommandRunner(new
                CommandDTO(null, LoginController.class,
                "Regist",new Object[]{"FinanceManager1","password1",User.USER_TYPE_FINANCEMANAGER},
                new Class[]{String.class,String.class,String.class})));
        t4.start();
    }
    private ResponseDTO run(CommandDTO dto) throws InterruptedException {
        CommandRunner runner = new CommandRunner(dto);
        Thread t = new Thread(runner);
        t.start();
        t.join();
        return runner.getResult();
    }
    public ResponseDTO login() throws InterruptedException {
        CommandDTO dto = new CommandDTO((User) new CourseManager().findAny(),CourseController.class,"newSemester",
                new Object[]{"2018 Spring Fall"},new Class[]{String.class});
        return this.run(dto);
    }
    public ResponseDTO newSemesterOperation() throws InterruptedException {
        CommandDTO dto = new CommandDTO((User) new CourseManager().findAny(),CourseController.class,"newSemester",
                new Object[]{"2018 Spring Fall"},new Class[]{String.class});
        return this.run(dto);
    }
    public ResponseDTO signUpClass(User user,String courseId) throws InterruptedException {
        CommandDTO dto = new CommandDTO((User) user,CourseController.class,"signUp",
                new Object[]{courseId},new Class[]{String.class});
        return this.run(dto);
    }
    public ResponseDTO dropCourse(User user,String courseId) throws InterruptedException {
        CommandDTO dto = new CommandDTO((User) user,CourseController.class,"dropCourse",
                new Object[]{courseId},new Class[]{String.class});
        return this.run(dto);
    }
    public ResponseDTO viewTranscript(User user,String semesterName) throws InterruptedException {
        CommandDTO dto = new CommandDTO((User) user,CourseController.class,"viewTranscript",
                new Object[]{semesterName},new Class[]{String.class});
        return this.run(dto);
    }
    public ResponseDTO createEvaludationRule(User user,long courseId,String type,double aValue,double bValue) throws InterruptedException {
        CommandDTO dto = new CommandDTO((User) user,CourseController.class,"createEvaludationRule",
                new Object[]{courseId,type,aValue,bValue},new Class[]{Long.class,String.class,Double.class,Double.class});
        return this.run(dto);
    }
    public ResponseDTO createCourse(User user,Course course) throws InterruptedException {
        CommandDTO dto = new CommandDTO((User) user,CourseController.class,"createCourse",
                new Object[]{course},new Class[]{Course.class});
        return this.run(dto);
    }
    public ResponseDTO deleteCourse(User user,Course course) throws InterruptedException {
        CommandDTO dto = new CommandDTO((User) user,CourseController.class,"deleteCourse",
                new Object[]{course},new Class[]{Course.class});
        return this.run(dto);
    }
    public ResponseDTO updateCourse(User user,Course course) throws InterruptedException {
        CommandDTO dto = new CommandDTO((User) user,CourseController.class,"updateCourse",
                new Object[]{course},new Class[]{Course.class});
        return this.run(dto);
    }
    public ResponseDTO viewRegistedCourse(User user,String semesterName) throws InterruptedException {
        CommandDTO dto = new CommandDTO((User) user,CourseController.class,"viewRegistedCourse",
                new Object[]{semesterName},new Class[]{String.class});
        return this.run(dto);
    }
    public ResponseDTO getRelatedCourse(User user,String semesterName) throws InterruptedException {
        CommandDTO dto = new CommandDTO((User) user,CourseController.class,"getRelatedCourse",
                new Object[]{semesterName},new Class[]{String.class});
        return this.run(dto);
    }
    public ResponseDTO gradeCourse(User user,long courseId) throws InterruptedException {
        CommandDTO dto = new CommandDTO((User) user,CourseController.class,"gradeCourse",
                new Object[]{courseId},new Class[]{Long.class});
        return this.run(dto);
    }
    public ResponseDTO browseAssignmentSubmission(User user,long assignmentId) throws InterruptedException {
        CommandDTO dto = new CommandDTO((User) user, AssignmentController.class,"browseAssignmentSubmission",
                new Object[]{assignmentId},new Class[]{Long.class});
        return this.run(dto);
    }
    public ResponseDTO createAssignmentSubmission(User user,long assignmentId,String content) throws InterruptedException {
        CommandDTO dto = new CommandDTO((User) user, AssignmentController.class,"createAssignmentSubmission",
                new Object[]{assignmentId,content},new Class[]{Long.class,String.class});
        return this.run(dto);
    }
    public ResponseDTO updateAssignmentSubmission(User user,long assnId,String content) throws InterruptedException {
        CommandDTO dto = new CommandDTO((User) user, AssignmentController.class,"updateAssignmentSubmission",
                new Object[]{assnId,content},new Class[]{Long.class,String.class});
        return this.run(dto);
    }
    public ResponseDTO deleteAssignmentSubmission(User user,long assnSubId) throws InterruptedException {
        CommandDTO dto = new CommandDTO((User) user, AssignmentController.class,"deleteAssignmentSubmission",
                new Object[]{assnSubId},new Class[]{Long.class});
        return this.run(dto);
    }
    public ResponseDTO createAssignment(User user, Assignment assn) throws InterruptedException {
        CommandDTO dto = new CommandDTO((User) user, AssignmentController.class,"createAssignment",
                new Object[]{assn},new Class[]{Assignment.class});
        return this.run(dto);
    }
    public ResponseDTO updateAssignment(User user, Assignment assn) throws InterruptedException {
        CommandDTO dto = new CommandDTO((User) user, AssignmentController.class,"updateAssignment",
                new Object[]{assn},new Class[]{Assignment.class});
        return this.run(dto);
    }
    public ResponseDTO browseAssignmentByCourse(User user, long courseId) throws InterruptedException {
        CommandDTO dto = new CommandDTO((User) user, AssignmentController.class,"browseAssignmentByCourse",
                new Object[]{courseId},new Class[]{Long.class});
        return this.run(dto);
    }
    public ResponseDTO deleteAssignment(User user, Assignment assn) throws InterruptedException {
        CommandDTO dto = new CommandDTO((User) user, AssignmentController.class,"deleteAssignment",
                new Object[]{assn},new Class[]{Assignment.class});
        return this.run(dto);
    }
    public ResponseDTO gradeAssignmentSubmission(User user,long assignmentSubId,int score) throws InterruptedException {
        CommandDTO dto = new CommandDTO((User) user, AssignmentController.class,"gradeAssignmentSubmission",
                new Object[]{assignmentSubId,score},new Class[]{Long.class,Integer.class});
        return this.run(dto);
    }
    public ResponseDTO addScholarShip(User user,ScholarShip ship) throws InterruptedException {
        CommandDTO dto = new CommandDTO((User) user, ScholarShipController.class,"addScholarShip",
                new Object[]{ship},new Class[]{ScholarShip.class});
        return this.run(dto);
    }
    public ResponseDTO deleteScholarShip(User user,ScholarShip ship) throws InterruptedException {
        CommandDTO dto = new CommandDTO((User) user, ScholarShipController.class,"deleteScholarShip",
                new Object[]{ship},new Class[]{ScholarShip.class});
        return this.run(dto);
    }
    public ResponseDTO browseScholarShip(User user) throws InterruptedException {
        CommandDTO dto = new CommandDTO((User) user, ScholarShipController.class,"browseScholarShip",
                new Object[]{},new Class[]{});
        return this.run(dto);
    }
    public ResponseDTO attachScholarShip(User user,long shipId,long studentId) throws InterruptedException {
        CommandDTO dto = new CommandDTO((User) user, ScholarShipController.class,"attachScholarShip",
                new Object[]{shipId,studentId},new Class[]{Long.class,Long.class});
        return this.run(dto);
    }
    public ResponseDTO deleteScholarShipAssignment(User user,long assngmentId) throws InterruptedException {
        CommandDTO dto = new CommandDTO((User) user, ScholarShipController.class,"deleteScholarShipAssignment",
                new Object[]{assngmentId},new Class[]{Long.class});
        return this.run(dto);
    }
    public ResponseDTO browseScholarShipAssignment(User user,long scholarShipId) throws InterruptedException {
        CommandDTO dto = new CommandDTO((User) user, ScholarShipController.class,"browseScholarShipAssignment",
                new Object[]{scholarShipId},new Class[]{Long.class});
        return this.run(dto);
    }
}
