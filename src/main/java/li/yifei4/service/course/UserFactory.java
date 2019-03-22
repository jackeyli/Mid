package li.yifei4.service.course;

import li.yifei4.data.entity.role.*;

public class UserFactory {
    public static User getUser(String type,String name,String password){
        switch(type){
            case User.USER_TYPE_STUDENT:
                return new Student(name,password);
            case User.USER_TYPE_TEACHER:
                return new Teacher(name,password);
            case User.USER_TYPE_FINANCEMANAGER:
                return new FinanceManager(name,password);
            case User.USER_TYPE_COURSEMANAGER:
                return new CourseManager(name,password);
        }
        return null;
    }
}
