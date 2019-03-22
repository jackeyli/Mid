package li.yifei4.tester;
import li.yifei4.data.entity.role.User;

public class CommandDTO {
    private User runner;
    private Class clz;
    private String method;
    private Object[] params;
    private Class[] paramClzes;
    public CommandDTO(User runner,Class clz,String method,Object[] params,Class[] paramClass){
        this.runner = runner;
        this.clz = clz;
        this.method = method;
        this.paramClzes = paramClass;
        this.params = params;
    }
    public User getRunner() {
        return runner;
    }

    public void setRunner(User runner) {
        this.runner = runner;
    }
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public Class[] getParamClzes() {
        return paramClzes;
    }

    public void setParamClzes(Class[] paramClzes) {
        this.paramClzes = paramClzes;
    }

    public Class getClz() {
        return clz;
    }

    public void setClz(Class clz) {
        this.clz = clz;
    }
}
