package li.yifei4.data.entity.scholarShip;

import li.yifei4.data.entity.role.Student;
import li.yifei4.data.table.DataTable;
import li.yifei4.data.table.tableManager.DataManager;

public class StatusBasedScholarShip extends ScholarShip{
    public StatusBasedScholarShip(){
        super(ScholarShipType.STATUSBASED);
    }
    public DataTable retrieveRelatedTable(){
        return DataManager.getTable(ScholarShip.class.getSimpleName());
    }
    @Override
    public boolean applyTo(Student student) {
        return true;
    }
}
