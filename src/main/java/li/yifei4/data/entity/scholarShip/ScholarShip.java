package li.yifei4.data.entity.scholarShip;

import li.yifei4.data.entity.Entity;
import li.yifei4.data.entity.role.Student;
import li.yifei4.data.table.tableManager.DataManager;

import java.util.List;
import java.util.stream.Collectors;

public class ScholarShip extends Entity implements ScholarshipInterface{
    public ScholarShip(){

    }
    public static final String DISCOUNT_TYPE_FIXEDRATE = "FIXEDRATE";
    public static final String DISCOUNT_TYPE_DISCOUNT = "DISCOUNT";
    public boolean calculateBalance(BalanceCalculationDTO dto){
        double balance = dto.getBalance();
        if(DISCOUNT_TYPE_FIXEDRATE.equals(this.getDiscountType())){
            dto.setBalance(balance - this.getFixedRate());
        }else {
            dto.setBalance(balance * (1 - this.getDiscount()));
        }
        return true;
    }

    public double getFixedRate() {
        return fixedRate;
    }

    public void setFixedRate(double fixedRate) {
        this.fixedRate = fixedRate;
    }
    public ScholarShip(ScholarShipType type){
        this.type  = type;
    }

    public static List<ScholarShip> getAllScholarShip(){
       return DataManager.getTable(ScholarShip.class.getSimpleName()).getTableData();
    }
    public static List<ScholarShip> findByStudent(long studentId){
       return (List<ScholarShip>) DataManager.getTable(ScholarShipAssignment.class.getSimpleName()).getTableData()
                .stream().filter((t)->((ScholarShipAssignment)t).getStudentId() == studentId)
                .map((t)->Entity.findById(ScholarShip.class,((ScholarShipAssignment) t).getScholarShipId()))
                .collect(Collectors.toList());
    }
    public ScholarShipType getType() {
        return type;
    }

    public void setType(ScholarShipType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean applyTo(Student student) {
        return false;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public enum ScholarShipType{
        STATUSBASED,MERITBASED
    }
    private double fixedRate;
    private String discountType;
    public void setFixedRate(long fixedRate) {
        this.fixedRate = fixedRate;
    }
    private ScholarShipType type;
    private String name;
    private double discount;
}
