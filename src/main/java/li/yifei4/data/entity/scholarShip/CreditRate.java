package li.yifei4.data.entity.scholarShip;

import li.yifei4.data.entity.Entity;
import li.yifei4.data.entity.course.Semester;
import li.yifei4.data.table.tableManager.DataManager;

import java.util.Optional;

public class CreditRate extends Entity {
    private double priceCredit;
    private long semesterId;
    public double getPriceCredit() {
        return priceCredit;
    }

    public void setPriceCredit(double priceCredit) {
        this.priceCredit = priceCredit;
    }
    public static CreditRate findCreditRate(String semesterName){
        Semester theSemester = Semester.findByName(semesterName);
        return (CreditRate) DataManager.getTable(CreditRate.class.getSimpleName())
                .getTableData()
                .stream().filter((t)->
                    ((CreditRate)t).getSemesterId() == theSemester.getId()
                ).findAny().orElse(null);
    }
    public long getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(long semesterId) {
        this.semesterId = semesterId;
    }
}
