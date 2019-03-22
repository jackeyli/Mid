package li.yifei4.controllers;

import li.yifei4.AssignmentSubmissionDTO;
import li.yifei4.ResponseDTO;
import li.yifei4.data.entity.course.Assignment;
import li.yifei4.data.entity.course.AssignmentGrade;
import li.yifei4.data.entity.course.AssignmentSubmission;
import li.yifei4.data.entity.role.User;
import li.yifei4.data.table.tableManager.DataManager;
import li.yifei4.data.transaction.Transaction;
import li.yifei4.data.transaction.TransactionManager;
import li.yifei4.logIn.UserAgent;
import li.yifei4.service.course.Assignmentable;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/assignment")
public class AssignmentController {
    @RequestMapping("/browseAssignmentSubmission")
    @ResponseBody
    public ResponseDTO browseAssignmentSubmission(@RequestParam long assignmentId){
        ResponseDTO res = new ResponseDTO();
        Transaction readTransaction = TransactionManager.getInstance().createReadTransaction();
        readTransaction.registRead(DataManager.getTable(Assignment.class.getSimpleName()));
        readTransaction.registRead(DataManager.getTable(AssignmentSubmission.class.getSimpleName()));
        readTransaction.registRead(DataManager.getTable(User.class.getSimpleName()));
        readTransaction.beginRead();
        try {
            Assignmentable assn = (Assignmentable) UserAgent.INSTANCE.getInstance().getUser();
            res.setValue(assn.browseAssignmentSubmission(assignmentId).stream()
            .map((t)->{
                AssignmentSubmissionDTO nDto = new AssignmentSubmissionDTO();
                BeanUtils.copyProperties(t,nDto);
                Assignment rAssn = t.retrieveAssignment();
                if(Objects.equals(rAssn.getAssignType(), Assignment.AssignmentType.ESSAY)){
                    nDto.setContent(((AssignmentSubmission)t).getEssayContent());
                } else {
                    nDto.setContent(
                            Arrays.stream(((AssignmentSubmission)t)
                                    .getMultipleChoice())
                                    .collect(Collectors.joining(";")));
                }
                AssignmentGrade grade = AssignmentGrade.findByAssignmentSubmission(t.getId());
                if(grade != null) {
                    nDto.setGraded(true);
                    nDto.setPoint(grade.getScore());
                }
                return nDto;
            }).collect(Collectors.toList()));
        }catch (Throwable e){
            res.setErrorMessage(e.getMessage());
        }finally {
            try {
                TransactionManager.getInstance().commit();
            }catch(Throwable e){
                res.setErrorMessage(e.getMessage());
            }
            return res;
        }
    }
    @RequestMapping("/createAssignmentSubmission")
    @ResponseBody
    public ResponseDTO createAssignmentSubmission(@RequestParam long assnId,@RequestParam String content){
        ResponseDTO res = new ResponseDTO();
        Transaction readTransaction = TransactionManager.getInstance().createReadTransaction();
        readTransaction.registRead(DataManager.getTable(Assignment.class.getSimpleName()));
        readTransaction.registRead(DataManager.getTable(AssignmentSubmission.class.getSimpleName()));
        readTransaction.beginRead();
        try {
            TransactionManager.getInstance().beginWriteTransaction();
            Assignmentable assn = (Assignmentable) UserAgent.INSTANCE.getInstance().getUser();
            assn.createAssignmentSubmission(assnId,content);
            res.setValue("Done!");
        }catch (Throwable e){
            res.setErrorMessage(e.getMessage());
        }finally {
            try {
                TransactionManager.getInstance().commit();
            }catch(Throwable e){
                res.setErrorMessage(e.getMessage());
            }
            return res;
        }
    }
    @RequestMapping("/updateAssignmentSubmission")
    @ResponseBody
    public ResponseDTO updateAssignmentSubmission(@RequestParam long assnId,@RequestParam String content){
        ResponseDTO res = new ResponseDTO();
        Transaction readTransaction = TransactionManager.getInstance().createReadTransaction();
        readTransaction.registRead(DataManager.getTable(Assignment.class.getSimpleName()));
        readTransaction.registRead(DataManager.getTable(AssignmentSubmission.class.getSimpleName()));
        readTransaction.beginRead();
        try {
            TransactionManager.getInstance().beginWriteTransaction();
            Assignmentable assn = (Assignmentable) UserAgent.INSTANCE.getInstance().getUser();
            assn.updateAssignmentSubmission(assnId,content);
            res.setValue("Done!");
        }catch (Throwable e){
            res.setErrorMessage(e.getMessage());
        }finally {
            try {
                TransactionManager.getInstance().commit();
            }catch(Throwable e){
                res.setErrorMessage(e.getMessage());
            }
            return res;
        }
    }
    @RequestMapping("/deleteAssignmentSubmission")
    @ResponseBody
    public ResponseDTO deleteAssignmentSubmission(@RequestParam long assnSubId){
        ResponseDTO res = new ResponseDTO();
        Transaction readTransaction = TransactionManager.getInstance().createReadTransaction();
        readTransaction.registRead(DataManager.getTable(Assignment.class.getSimpleName()));
        readTransaction.registRead(DataManager.getTable(AssignmentSubmission.class.getSimpleName()));
        readTransaction.beginRead();
        try {
            TransactionManager.getInstance().beginWriteTransaction();
            Assignmentable assn = (Assignmentable) UserAgent.INSTANCE.getInstance().getUser();
            assn.deleteAssignmentSubmission(assnSubId);
            res.setValue("Done!");
        }catch (Throwable e){
            res.setErrorMessage(e.getMessage());
        }finally {
            try {
                TransactionManager.getInstance().commit();
            }catch(Throwable e){
                res.setErrorMessage(e.getMessage());
            }
            return res;
        }
    }
    @RequestMapping("/createAssignment")
    @ResponseBody
    public ResponseDTO createAssignment(@RequestBody Assignment assn){
        ResponseDTO res = new ResponseDTO();
        try {
            TransactionManager.getInstance().beginWriteTransaction();
            Assignmentable assnTable = (Assignmentable) UserAgent.INSTANCE.getInstance().getUser();
            assnTable.createAssignment(assn);
            res.setValue("Done!");
        }catch (Throwable e){
            res.setErrorMessage(e.getMessage());
        }finally {
            try {
                TransactionManager.getInstance().commit();
            }catch(Throwable e){
                res.setErrorMessage(e.getMessage());
            }
            return res;
        }
    }
    @RequestMapping("/updateAssignment")
    @ResponseBody
    public ResponseDTO updateAssignment(@RequestBody Assignment assn){
        ResponseDTO res = new ResponseDTO();
        try {
            TransactionManager.getInstance().beginWriteTransaction();
            Assignmentable assnTable = (Assignmentable) UserAgent.INSTANCE.getInstance().getUser();
            assnTable.updateAssignment(assn);
            res.setValue("Done!");
        }catch (Throwable e){
            res.setErrorMessage(e.getMessage());
        }finally {
            try {
                TransactionManager.getInstance().commit();
            }catch(Throwable e){
                res.setErrorMessage(e.getMessage());
            }
            return res;
        }
    }
    @RequestMapping("/browseAssignmentByCourse")
    @ResponseBody
    public ResponseDTO browseAssignmentByCourse(@RequestParam long courseId){
        ResponseDTO res = new ResponseDTO();
        Transaction readTransaction = TransactionManager.getInstance().createReadTransaction();
        readTransaction.registRead(DataManager.getTable(Assignment.class.getSimpleName()));
        readTransaction.beginRead();
        try {
            res.setValue(Assignment.findByCourse(courseId));
        }catch (Throwable e){
            res.setErrorMessage(e.getMessage());
        }finally {
            try {
                TransactionManager.getInstance().commit();
            }catch(Throwable e){
                res.setErrorMessage(e.getMessage());
            }
            return res;
        }
    }
    @RequestMapping("/deleteAssignment")
    @ResponseBody
    public ResponseDTO deleteAssignment(@RequestBody Assignment assn){
        ResponseDTO res = new ResponseDTO();
        Transaction read = TransactionManager.getInstance().createReadTransaction();
        read.registRead(DataManager.getTable(AssignmentSubmission.class.getSimpleName()));
        read.registRead(DataManager.getTable(AssignmentGrade.class.getSimpleName()));
        read.registRead(DataManager.getTable(Assignment.class.getSimpleName()));
        read.beginRead();
        try {
            TransactionManager.getInstance().beginWriteTransaction();
            Assignmentable assnTable = (Assignmentable) UserAgent.INSTANCE.getInstance().getUser();
            assnTable.deleteAssignment(assn);
            res.setValue("Done!");
        }catch (Throwable e){
            res.setErrorMessage(e.getMessage());
        }finally {
            try {
                TransactionManager.getInstance().commit();
            }catch(Throwable e){
                res.setErrorMessage(e.getMessage());
            }
            return res;
        }
    }
    @RequestMapping("/gradeAssignmentSubmission")
    @ResponseBody
    public ResponseDTO gradeAssignmentSubmission(@RequestParam long assignmentSubId,@RequestParam int score){
        ResponseDTO res = new ResponseDTO();
        try {
            TransactionManager.getInstance().beginWriteTransaction();
            Assignmentable assntable = (Assignmentable) UserAgent.INSTANCE.getInstance().getUser();
            assntable.gradeAssignment(assignmentSubId,score);
            res.setValue("Done!");
        }catch (Throwable e){
            res.setErrorMessage(e.getMessage());
        }finally {
            try {
                TransactionManager.getInstance().commit();
            }catch(Throwable e){
                res.setErrorMessage(e.getMessage());
            }
            return res;
        }
    }
}
