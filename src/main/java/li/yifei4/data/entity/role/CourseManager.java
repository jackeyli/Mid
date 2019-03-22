package li.yifei4.data.entity.role;

import li.yifei4.data.entity.course.Course;
import li.yifei4.data.entity.course.CourseRegDocument;
import li.yifei4.data.table.DataTable;
import li.yifei4.data.table.tableManager.DataManager;

import java.util.List;

public class CourseManager extends User{
    private String userType = User.USER_TYPE_COURSEMANAGER;
    public CourseManager(){

    }
    public CourseManager(String name, String password) {
        super(name, password);
    }

    public void createCourse(Course course){
        course.insert();
    }
    public void deleteCourse(Course course){
        course.delete();
        CourseRegDocument.findByCourse(course.getId()).stream()
                .forEach((t)->{t.delete();});
    }
    public void updateCourse(Course course){
        course.update();
    }
    public DataTable retrieveRelatedTable(){
        return DataManager.getTable(User.class.getSimpleName());
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
