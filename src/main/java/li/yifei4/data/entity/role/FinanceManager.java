package li.yifei4.data.entity.role;

import li.yifei4.ScholarShipAssignmentDTO;
import li.yifei4.data.entity.Entity;
import li.yifei4.data.entity.course.Semester;
import li.yifei4.data.entity.scholarShip.CreditRate;
import li.yifei4.data.entity.scholarShip.ScholarShip;
import li.yifei4.data.entity.scholarShip.ScholarShipAssignment;
import li.yifei4.data.table.DataTable;
import li.yifei4.data.table.tableManager.DataManager;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.criteria.CriteriaDelete;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FinanceManager extends User {
    private String userType = User.USER_TYPE_FINANCEMANAGER;

    public FinanceManager(String name, String password) {
        super(name, password);
    }
    public FinanceManager(){

    }
    public void addScholarShip(ScholarShip sship){
        sship.insert();
    }
    public void updateScholarShip(ScholarShip sship){
        sship.update();
    }
    public void deleteScholarShip(ScholarShip sship){
        sship.delete();
        ScholarShipAssignment.findByScholarShip(sship.getId()).stream()
                .forEach((t)->t.delete());
    }
    public List<ScholarShip> browseScholarShip(){
       return ScholarShip.getAllScholarShip();
    }
    public void attachScholarShip(long shipId,long studentId){
        if(ScholarShipAssignment.findByScholarShip(shipId).stream().anyMatch(t->t.getStudentId() == studentId)){
            throw new RuntimeException("The student has already got this scholarship");
        }
        ScholarShipAssignment newAssignment = new ScholarShipAssignment();
        newAssignment.setScholarShipId(shipId);
        newAssignment.setStudentId(studentId);
        newAssignment.insert();
    }
    public void updateCreditRate(String semesterName,double rate){
        CreditRate creditRate =  CreditRate.findCreditRate(semesterName);
        if(creditRate == null){
            creditRate = new CreditRate();
            creditRate.setSemesterId(Semester.findByName(semesterName).getId());
            creditRate.setPriceCredit(rate);
            creditRate.insert();
        } else {
            creditRate.setPriceCredit(rate);
            creditRate.update();
        }
    }
    public DataTable retrieveRelatedTable(){
        return DataManager.getTable(User.class.getSimpleName());
    }
    public void deleteScholarShipAssignment(long assignmentId){
        ScholarShipAssignment assn = (ScholarShipAssignment)Entity.findById(ScholarShipAssignment.class,assignmentId);
        if(assn != null)
            assn.delete();
    }
    public List<ScholarShipAssignmentDTO> browseScholarShipAssignment(long scholarShipId){
        return ScholarShipAssignment
                .findByScholarShip(scholarShipId)
                .stream().map((t)->{
                    ScholarShipAssignmentDTO dto = new ScholarShipAssignmentDTO();
                    dto.setScholarShipName(Optional.ofNullable((ScholarShip)Entity.findById(ScholarShip.class,
                            t.getScholarShipId())).map((p)->p.getName()).orElse(null));
                    dto.setStudentName(Optional.ofNullable((User)Entity.findById(User.class,
                            t.getStudentId())).map((p)->p.getName()).orElse(null));
                    BeanUtils.copyProperties(t,dto);
                    return dto;
                }).collect(Collectors.toList());
    }

    public String getUserType() {
        return userType;
    }
    public void setUserType(String userType) {
        this.userType = userType;
    }
}
