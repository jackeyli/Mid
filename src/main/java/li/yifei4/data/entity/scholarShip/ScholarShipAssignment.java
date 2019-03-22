package li.yifei4.data.entity.scholarShip;

import li.yifei4.data.entity.Entity;
import li.yifei4.data.table.tableManager.DataManager;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.stream.Collectors;

public class ScholarShipAssignment extends Entity {
    private long studentId = -1;
    private long scholarShipId = -1;

    public static List<ScholarShipAssignment> findByScholarShip(long scholarShipId){
        return (List<ScholarShipAssignment>) DataManager.getTable(ScholarShipAssignment.class.getSimpleName())
                .getTableData().stream().filter(t->((ScholarShipAssignment)t).scholarShipId == scholarShipId)
                .collect(Collectors.toList());
    }
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
}
