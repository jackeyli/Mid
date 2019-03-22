package li.yifei4.controllers;

import li.yifei4.CourseDTO;
import li.yifei4.ResponseDTO;
import li.yifei4.data.entity.Entity;
import li.yifei4.data.entity.course.*;
import li.yifei4.data.entity.role.*;
import li.yifei4.data.entity.scholarShip.CreditRate;
import li.yifei4.data.entity.scholarShip.ScholarShip;
import li.yifei4.data.table.tableManager.DataManager;
import li.yifei4.data.transaction.Transaction;
import li.yifei4.data.transaction.TransactionManager;
import li.yifei4.logIn.UserAgent;
import li.yifei4.service.course.Assignmentable;
import li.yifei4.service.course.EvaluationRuleFactory;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/course")
public class CourseController {
    @RequestMapping("/getAllTeacher")
    @ResponseBody
    public ResponseDTO getAllTeacher() {
        ResponseDTO resp = new ResponseDTO();
        Transaction readTransaction = TransactionManager.getInstance().createReadTransaction();
        readTransaction.registRead(DataManager.getTable(User.class.getSimpleName()));
        readTransaction.beginRead();
        try {
            resp.setValue(Teacher.findAll());
        }catch(Throwable e){
            resp.setErrorMessage(e.getMessage());
        }finally{
            try {
                TransactionManager.getInstance().commit();
            }catch(Throwable e){
                resp.setErrorMessage(e.getMessage());
            }
            return resp;
        }
    }

    @RequestMapping("/newSemester")
    @ResponseBody
    public ResponseDTO newSemester(@RequestParam String semesterName) {
        ResponseDTO resp = new ResponseDTO();
        TransactionManager.getInstance().beginWriteTransaction();
        try {
            Semester thisSemester = new Semester();
            thisSemester.setName(semesterName);
            thisSemester.insert();
            resp.setValue("Done!");
        }catch(Throwable e){
            resp.setErrorMessage(e.getMessage());
        }finally{
            try {
                TransactionManager.getInstance().commit();
            }catch(Throwable e){
                resp.setErrorMessage(e.getMessage());
            }
            return resp;
        }
    }

    @RequestMapping("/getAllSemester")
    @ResponseBody
    public ResponseDTO getAllSemester() {
        ResponseDTO resp = new ResponseDTO();
        Transaction readTransaction = TransactionManager.getInstance().createReadTransaction();
        readTransaction.registRead(DataManager.getTable(Semester.class.getSimpleName()));
        readTransaction.beginRead();
        try {
            resp.setValue(Semester.findAll());
        }catch(Throwable e){
            resp.setErrorMessage(e.getMessage());
        }finally{
            try {
                TransactionManager.getInstance().commit();
            }catch(Throwable e){
                resp.setErrorMessage(e.getMessage());
            }
            return resp;
        }
    }
    @RequestMapping("/browseCourseBySemester")
    @ResponseBody
    public ResponseDTO browseCourseBySemester(@RequestParam String semesterName) {
        ResponseDTO resp = new ResponseDTO();
            Transaction readTransaction = TransactionManager.getInstance().createReadTransaction();
            readTransaction.registRead(DataManager.getTable(Course.class.getSimpleName()));
            readTransaction.registRead(DataManager.getTable(EvaluationRule.class.getSimpleName()));
            readTransaction.beginRead();
        try {
            List<CourseDTO> returnCourses = UserAgent.INSTANCE.getInstance().getUser().browseCourseBySemester(semesterName);
            resp.setValue(returnCourses);
        }catch(Throwable e){
            resp.setErrorMessage(e.getMessage());
        }finally{
            try {
                TransactionManager.getInstance().commit();
            }catch(Throwable e){
                resp.setErrorMessage(e.getMessage());
            }
            return resp;
        }
    }
    @RequestMapping("/signUp")
    @ResponseBody
    public ResponseDTO signUp(@RequestParam long courseId) {
        ResponseDTO resp = new ResponseDTO();
            Transaction readTransaction = TransactionManager.getInstance().createReadTransaction();
            readTransaction.registRead(DataManager.getTable(Course.class.getSimpleName()));
            readTransaction.registRead(DataManager.getTable(CourseRegDocument.class.getSimpleName()));
            readTransaction.beginRead();
            TransactionManager.getInstance().beginWriteTransaction();
        try {
            User thisUser = UserAgent.INSTANCE.getInstance().getUser();
            if(!(thisUser instanceof Student)){
                throw new RuntimeException("You cannot signUp Course");
            }
            Student thisStudent = (Student)thisUser;
            thisStudent.signUpCourse((Course)Entity.findById(Course.class,courseId));
            resp.setValue("Your course has been successfully signed Up");
        }catch(Throwable e){
            resp.setErrorMessage(e.getMessage());
        }finally {
            TransactionManager.getInstance().commit();
            return resp;
        }
    }
    @RequestMapping("/dropCourse")
    @ResponseBody
    public ResponseDTO dropCourse(@RequestParam long courseId) throws IOException, ClientProtocolException {
            ResponseDTO resp = new ResponseDTO();
            Transaction readTransaction = TransactionManager.getInstance().createReadTransaction();
            readTransaction.registRead(DataManager.getTable(Course.class.getSimpleName()));
            readTransaction.registRead(DataManager.getTable(CourseRegDocument.class.getSimpleName()));
            readTransaction.beginRead();
            TransactionManager.getInstance().beginWriteTransaction();
        try{
            User thisUser = UserAgent.INSTANCE.getInstance().getUser();
            if(!(thisUser instanceof Student)){
                throw new RuntimeException("You cannot drop Course");
            }
            Student thisStudent = (Student)thisUser;
            thisStudent.dropCourse((Course)Entity.findById(Course.class,courseId));
            resp.setValue("Your course has been successfully dropped");
        }catch(Throwable e){
            resp.setErrorMessage(e.getMessage());
        }finally {
            try {
                TransactionManager.getInstance().commit();
            }catch(Throwable e){
                resp.setErrorMessage(e.getMessage());
            }
            return resp;
        }
    }
    @RequestMapping("/viewTranscript")
    @ResponseBody
    public ResponseDTO viewTranscript(@RequestParam String semesterName) throws IOException, ClientProtocolException {
        ResponseDTO resp = new ResponseDTO();

            Transaction readTransaction = TransactionManager.getInstance().createReadTransaction();
            readTransaction.registRead(DataManager.getTable(Grade.class.getSimpleName()));
            readTransaction.registRead(DataManager.getTable(CourseRegDocument.class.getSimpleName()));
            readTransaction.registRead(DataManager.getTable(Course.class.getSimpleName()));
            readTransaction.beginRead();
            TransactionManager.getInstance().beginWriteTransaction();
        try {
            User thisUser = UserAgent.INSTANCE.getInstance().getUser();
            if(!(thisUser instanceof Student)){
                throw new RuntimeException("You are not a student");
            }
            Student thisStudent = (Student)thisUser;
            resp.setValue(thisStudent.viewTranscript(semesterName));
            return resp;
        }catch(Throwable e){
            resp.setErrorMessage(e.getMessage());
        }finally {
            try {
                TransactionManager.getInstance().commit();
            }catch(Throwable e){
                resp.setErrorMessage(e.getMessage());
            }
            return resp;
        }
    }
    @RequestMapping("/createEvaluationRule")
    @ResponseBody
    public ResponseDTO createEvaluationRule(@RequestParam long courseId,@RequestParam String type,@RequestParam double aValue, @RequestParam double bValue){
        ResponseDTO resp = new ResponseDTO();
        Transaction read = TransactionManager.getInstance().createReadTransaction();
        read.registRead(DataManager.getTable(Course.class.getSimpleName()));
        read.beginRead();
        try {
            TransactionManager.getInstance().beginWriteTransaction();
            User thisUser = UserAgent.INSTANCE.getInstance().getUser();
            if(!(thisUser instanceof Teacher)){
                throw new RuntimeException("You cannot createEvaluationRule");
            }
            Course course = (Course) Entity.findById(Course.class,courseId);
            EvaluationRule rule = EvaluationRuleFactory.getEvaluationRule(type, aValue, bValue);
            rule.insert();
            course.setEvaluationRule(rule.getId());
            course.update();
            resp.setValue("Done!");
        }catch(Throwable e){
            resp.setErrorMessage(e.getMessage());
        }finally{
            try {
                TransactionManager.getInstance().commit();
            }catch(Throwable e){
                resp.setErrorMessage(e.getMessage());
            }
            return resp;
        }
    }
    @RequestMapping("/createCourse")
    @ResponseBody
    public ResponseDTO createCourse(@RequestBody Course course){
        ResponseDTO resp = new ResponseDTO();
        try {
            TransactionManager.getInstance().beginWriteTransaction();
            ((CourseManager)UserAgent.INSTANCE.getInstance().getUser()).createCourse(course);
            resp.setValue("Done!");
        }catch(Throwable e)
        {
            resp.setErrorMessage(e.getMessage());
        }finally{
            try {
                TransactionManager.getInstance().commit();
            }catch(Throwable e){
                resp.setErrorMessage(e.getMessage());
            }
            return resp;
        }
    }
    @RequestMapping("/deleteCourse")
    @ResponseBody
    public ResponseDTO deleteCourse(@RequestBody Course course){
        ResponseDTO resp = new ResponseDTO();
        Transaction read = TransactionManager.getInstance().createReadTransaction();
        read.registRead(DataManager.getTable(CourseRegDocument.class.getSimpleName()));
        read.beginRead();
        try {
            TransactionManager.getInstance().beginWriteTransaction();
            ((CourseManager)UserAgent.INSTANCE.getInstance().getUser()).deleteCourse(course);
            resp.setValue("Done!");
        }finally{
            try {
                TransactionManager.getInstance().commit();
            }catch(Throwable e){
                resp.setErrorMessage(e.getMessage());
            }
            return resp;
        }
    }
    @RequestMapping("/updateCourse")
    @ResponseBody
    public ResponseDTO updateCourse(@RequestBody Course course){
        ResponseDTO resp = new ResponseDTO();
        try {
            TransactionManager.getInstance().beginWriteTransaction();
            ((CourseManager)UserAgent.INSTANCE.getInstance().getUser()).updateCourse(course);
            resp.setValue("Done!");
        }finally{
            try {
                TransactionManager.getInstance().commit();
            }catch(Throwable e){
                resp.setErrorMessage(e.getMessage());
            }
            return resp;
        }
    }
    @RequestMapping("/viewRegistedCourse")
    @ResponseBody
    public ResponseDTO viewRegistedCourse(@RequestParam String semesterName){
        ResponseDTO resp = new ResponseDTO();
        Transaction readTransaction = TransactionManager.getInstance().createReadTransaction();
        readTransaction.registRead(DataManager.getTable(Course.class.getSimpleName()));
        readTransaction.registRead(DataManager.getTable(CourseRegDocument.class.getSimpleName()));
        readTransaction.registRead(DataManager.getTable(EvaluationRule.class.getSimpleName()));
        readTransaction.registRead(DataManager.getTable(User.class.getSimpleName()));
        readTransaction.beginRead();
        try {
            User user  = UserAgent.INSTANCE.getInstance().getUser();
            if(!(user instanceof Student)){
                throw new RuntimeException("You are not a student");
            }
            Semester theSemester = Semester.findByName(semesterName);
            resp.setValue(CourseRegDocument.findStudentSemester(theSemester.getId(),user.getId()).stream().map(
                    (t)->{
                        CourseDTO ret = new CourseDTO();
                        Course course = (Course) Entity.findById(Course.class,t.getCourseId());
                        BeanUtils.copyProperties(course,ret);
                        ret.setRegistedCount(CourseRegDocument.findByCourse(((CourseRegDocument)t).getCourseId()).size());
                        return ret;
                    }
            ).collect(Collectors.toList()));
        }catch(Throwable e){
            resp.setErrorMessage(e.getMessage());
        }finally{
            try {
                TransactionManager.getInstance().commit();
            }catch(Throwable e){
                resp.setErrorMessage(e.getMessage());
            }
            return resp;
        }
    }
    @RequestMapping("/relatedCourse")
    @ResponseBody
    public ResponseDTO getRelatedCourse(@RequestParam String semesterName){
        ResponseDTO resp = new ResponseDTO();
        Transaction read = TransactionManager.getInstance().createReadTransaction();
        read.registRead(DataManager.getTable(User.class.getSimpleName()));
        read.registRead(DataManager.getTable(Course.class.getSimpleName()));
        read.registRead(DataManager.getTable(CourseRegDocument.class.getSimpleName()));
        read.beginRead();
        try {
            Assignmentable thisUser = (Assignmentable)UserAgent.INSTANCE.getInstance().getUser();
            resp.setValue(thisUser.retrieveRelatedCourse(semesterName));
        }finally{
            try {
                TransactionManager.getInstance().commit();
            }catch(Throwable e){
                resp.setErrorMessage(e.getMessage());
            }
            return resp;
        }
    }
    @RequestMapping("/gradeCourse")
    @ResponseBody
    public ResponseDTO gradeCourse(@RequestParam long courseId){
        ResponseDTO resp = new ResponseDTO();
        Transaction read = TransactionManager.getInstance().createReadTransaction();
        read.registRead(DataManager.getTable(User.class.getSimpleName()));
        read.registRead(DataManager.getTable(Course.class.getSimpleName()));
        read.registRead(DataManager.getTable(CourseRegDocument.class.getSimpleName()));
        read.registRead(DataManager.getTable(EvaluationRule.class.getSimpleName()));
        read.registRead(DataManager.getTable(AssignmentGrade.class.getSimpleName()));
        read.registRead(DataManager.getTable(Grade.class.getSimpleName()));
        read.beginRead();
        try {
            TransactionManager.getInstance().beginWriteTransaction();
             User thisUser = UserAgent.INSTANCE.getInstance().getUser();
             if(!(thisUser instanceof  Teacher)){
                 throw new RuntimeException("You cannot grade Course");
             }
            ((Teacher)thisUser).gradeCourse(courseId);
            resp.setValue("Done!");
        }catch (Throwable e){
            resp.setErrorMessage(e.getMessage());
        }finally{
            try {
                TransactionManager.getInstance().commit();
            }catch(Throwable e){
                resp.setErrorMessage(e.getMessage());
            }
            return resp;
        }
    }
    @RequestMapping("/viewCreditRate")
    @ResponseBody
    public ResponseDTO viewCreditRate(@RequestParam String semesterName){
        ResponseDTO resp = new ResponseDTO();
        Transaction read = TransactionManager.getInstance().createReadTransaction();
        read.registRead(DataManager.getTable(Semester.class.getSimpleName()));
        read.registRead(DataManager.getTable(CreditRate.class.getSimpleName()));
        read.beginRead();
        try {
            TransactionManager.getInstance().beginWriteTransaction();
            resp.setValue(UserAgent.INSTANCE.getInstance().getUser().viewCreditRate(semesterName));
        }catch (Throwable e){
            resp.setErrorMessage(e.getMessage());
        }finally{
            try {
                TransactionManager.getInstance().commit();
            }catch(Throwable e){
                resp.setErrorMessage(e.getMessage());
            }
            return resp;
        }
    }
    @RequestMapping("/updateCreditRate")
    @ResponseBody
    public ResponseDTO updateCreditRate(@RequestParam String semesterName,@RequestParam double rate){
        ResponseDTO resp = new ResponseDTO();
        Transaction read = TransactionManager.getInstance().createReadTransaction();
        read.registRead(DataManager.getTable(Semester.class.getSimpleName()));
        read.registRead(DataManager.getTable(CreditRate.class.getSimpleName()));
        read.beginRead();
        try {
            TransactionManager.getInstance().beginWriteTransaction();
            ((FinanceManager)UserAgent.INSTANCE.getInstance().getUser()).updateCreditRate(semesterName,rate);
            resp.setValue("Done!");
        }catch (Throwable e){
            resp.setErrorMessage(e.getMessage());
        }finally{
            try {
                TransactionManager.getInstance().commit();
            }catch(Throwable e){
                resp.setErrorMessage(e.getMessage());
            }
            return resp;
        }
    }
    @RequestMapping("/calculateBalance")
    @ResponseBody
    public ResponseDTO updateCreditRate(@RequestParam String semesterName){
        ResponseDTO resp = new ResponseDTO();
        Transaction read = TransactionManager.getInstance().createReadTransaction();
        read.registRead(DataManager.getTable(Semester.class.getSimpleName()));
        read.registRead(DataManager.getTable(CreditRate.class.getSimpleName()));
        read.beginRead();
        try {
            TransactionManager.getInstance().beginWriteTransaction();
            resp.setValue(((Student)UserAgent.INSTANCE.getInstance().getUser()).calculateMyBalance(semesterName));
        }catch (Throwable e){
            resp.setErrorMessage(e.getMessage());
        }finally{
            try {
                TransactionManager.getInstance().commit();
            }catch(Throwable e){
                resp.setErrorMessage(e.getMessage());
            }
            return resp;
        }
    }
}
