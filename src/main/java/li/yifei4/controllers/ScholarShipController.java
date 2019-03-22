package li.yifei4.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import li.yifei4.ResponseDTO;
import li.yifei4.data.entity.role.FinanceManager;
import li.yifei4.data.entity.role.User;
import li.yifei4.data.entity.scholarShip.ScholarShip;
import li.yifei4.data.entity.scholarShip.ScholarShipAssignment;
import li.yifei4.data.table.tableManager.DataManager;
import li.yifei4.data.transaction.Transaction;
import li.yifei4.data.transaction.TransactionManager;
import li.yifei4.logIn.UserAgent;
import li.yifei4.service.course.ScholarshipFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/scholarShip")
public class ScholarShipController {
    @RequestMapping("/addScholarShip")
    @ResponseBody
    public ResponseDTO addScholarShip(@RequestParam String shipJson){
        ResponseDTO resp = new ResponseDTO();
        TransactionManager.getInstance().beginWriteTransaction();
        Gson gson = new GsonBuilder().create();
        ScholarShip ship = gson.fromJson(shipJson,ScholarShip.class);
        try {
            ((FinanceManager) UserAgent.INSTANCE.getInstance().getUser())
                    .addScholarShip(ScholarshipFactory.getScholarship(ship.getType().name(),ship));
            resp.setValue("Done!");
        }catch(Throwable e) {
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
    @RequestMapping("/updateScholarShip")
    @ResponseBody
    public ResponseDTO updateScholarShip(@RequestParam String shipJson){
        ResponseDTO resp = new ResponseDTO();
        TransactionManager.getInstance().beginWriteTransaction();
        Gson gson = new GsonBuilder().create();
        ScholarShip ship = gson.fromJson(shipJson,ScholarShip.class);
        try {
            ((FinanceManager) UserAgent.INSTANCE.getInstance().getUser()).updateScholarShip(ship);
            resp.setValue("Done!");
        }catch(Throwable e) {
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
    @RequestMapping("/deleteScholarShip")
    @ResponseBody
    public ResponseDTO deleteScholarShip(@RequestParam String shipJson){
        ResponseDTO resp = new ResponseDTO();
        TransactionManager.getInstance().beginWriteTransaction();
        Gson gson = new GsonBuilder().create();
        ScholarShip ship = gson.fromJson(shipJson,ScholarShip.class);
        try {
            ((FinanceManager) UserAgent.INSTANCE.getInstance().getUser()).deleteScholarShip(ship);
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
    @RequestMapping("/browseScholarShip")
    @ResponseBody
    public ResponseDTO browseScholarShip(){
        ResponseDTO resp = new ResponseDTO();
        Transaction trans = TransactionManager.getInstance().createReadTransaction();
        trans.registRead(DataManager.getTable(ScholarShip.class.getSimpleName()));
        trans.beginRead();
        try {
            User thisUsr = UserAgent.INSTANCE.getInstance().getUser();
            resp.setValue(((FinanceManager) UserAgent.INSTANCE.getInstance().getUser()).browseScholarShip());
        }catch(Throwable e) {
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
    @RequestMapping("/attachScholarShip")
    @ResponseBody
    public ResponseDTO attachScholarShip(@RequestParam long shipId,@RequestParam long studentId){
        ResponseDTO resp = new ResponseDTO();
        Transaction trans = TransactionManager.getInstance().createReadTransaction();
        trans.registRead(DataManager.getTable(ScholarShipAssignment.class.getSimpleName()));
        trans.beginRead();
        TransactionManager.getInstance().beginWriteTransaction();
        try {
            ((FinanceManager) UserAgent.INSTANCE.getInstance().getUser()).attachScholarShip(shipId,studentId);
            resp.setValue("Done");
        }catch(Throwable e) {
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

    @RequestMapping("/deleteScholarShipAssignment")
    @ResponseBody
    public ResponseDTO deleteScholarShipAssignment(long assignmentId){
        ResponseDTO resp = new ResponseDTO();
        Transaction trans = TransactionManager.getInstance().createReadTransaction();
        trans.registRead(DataManager.getTable(ScholarShipAssignment.class.getSimpleName()));
        trans.beginRead();
        TransactionManager.getInstance().beginWriteTransaction();
        try {
            ((FinanceManager) UserAgent.INSTANCE.getInstance().getUser()).deleteScholarShipAssignment(assignmentId);
            resp.setValue("Done!");
        }catch(Throwable e) {
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
    @RequestMapping("/browseScholarShipAssignment")
    @ResponseBody
    public ResponseDTO browseScholarShipAssignment(long scholarShipId){
        ResponseDTO resp = new ResponseDTO();
        Transaction trans = TransactionManager.getInstance().createReadTransaction();
        trans.registRead(DataManager.getTable(ScholarShipAssignment.class.getSimpleName()));
        trans.beginRead();
        try {
            resp.setValue(((FinanceManager) UserAgent.INSTANCE.getInstance().getUser()).browseScholarShipAssignment(scholarShipId));
        }catch(Throwable e) {
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
