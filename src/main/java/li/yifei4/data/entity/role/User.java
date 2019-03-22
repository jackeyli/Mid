package li.yifei4.data.entity.role;

import li.yifei4.data.entity.Entity;
import li.yifei4.data.entity.course.Course;
import li.yifei4.data.entity.scholarShip.CreditRate;

import java.util.List;

public abstract class User extends Entity {
    public static final String USER_TYPE_STUDENT = "STUDENT";
    public static final String USER_TYPE_TEACHER = "TEACHER";
    public static final String USER_TYPE_FINANCEMANAGER = "FINANCEMANAGER";
    public static final String USER_TYPE_COURSEMANAGER = "COURSEMANAGER";
    private String name;
    private String password;
    public User(){

    }
    public User(String name,String password){
        this.name = name;
        this.password = password;
    }
    public CreditRate viewCreditRate(String semesterName){
        return CreditRate.findCreditRate(semesterName);
    }
    public List browseCourseBySemester(String semester){
        return Course.findBySemester(semester);
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
