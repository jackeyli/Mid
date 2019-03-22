package li.yifei4.logIn;

import li.yifei4.data.entity.role.User;
public enum UserAgent {
    INSTANCE;
    private UserAgent.UserHolder user;
    UserAgent(){
        user = new UserAgent.UserHolder();
    }
    public UserHolder getInstance(){
        return user;
    }
    public class UserHolder {
        private ThreadLocal<User> rLocal = new ThreadLocal<User>();
        public User getUser(){
            return rLocal.get();
        }
        public void setUser(User user){
            rLocal.set(user);
        }
    }
}
