package li.yifei4.data.entity.course;

import li.yifei4.data.entity.Entity;
import li.yifei4.data.table.tableManager.DataManager;

import java.util.List;
import java.util.Objects;

public class Semester extends Entity {
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public static Semester findByName(String name){
        return (Semester) DataManager.getTable(Semester.class.getSimpleName())
                .getTableData().stream().filter((t)-> Objects.equals(name,((Semester)t).getName()))
                .findAny().orElse(null);
    }
    public static List<Semester> findAll(){
        return DataManager.getTable(Semester.class.getSimpleName())
                .getTableData();
    }
}
