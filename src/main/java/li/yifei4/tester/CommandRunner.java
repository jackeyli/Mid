package li.yifei4.tester;

import li.yifei4.ResponseDTO;
import li.yifei4.logIn.UserAgent;

import javax.persistence.criteria.Predicate;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CommandRunner implements Runnable{
    private boolean isExecutingMethod(Method m , String methodName, int parameterCount, Class<?> ... paramClasses) {
        boolean isValid = Objects.equals(m.getName(),methodName) &&
                m.getParameterCount() == parameterCount;
        if(!isValid)
            return false;
        List<Class<?>> params = Arrays.asList(m.getParameterTypes());
        List<Class<?>> inputParam = Arrays.asList(paramClasses);
        for(int i = 0; i < inputParam.size(); i ++)
        {
            Class<?> mClass = params.get(i);
            isValid = isValid && mClass.isAssignableFrom(inputParam.get(i));
        }
        return isValid;
    }
    private Method getExecuteMethod(Class clz,String methodName,Class<?> ...paramClasses){
        List<Method> methods = Arrays.asList(clz.getDeclaredMethods()).stream()
                .filter(m->isExecutingMethod(m,methodName,paramClasses.length,paramClasses)).collect(Collectors.toList());
        if(methods.size() == 0)
            throw new RuntimeException("No Such Method");
        return methods.get(0);
    }
    private Object executeCommand(CommandDTO dto) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Method method = this.getExecuteMethod(dto.getClz(),dto.getMethod(),dto.getParamClzes());
        UserAgent.INSTANCE.getInstance().setUser(dto.getRunner());
        return  method.invoke(dto.getClass().newInstance(),dto.getParams());
    }
    private CommandDTO dto;
    public CommandRunner(CommandDTO dto){
        this.dto = dto;
    }

    public ResponseDTO getResult() {
        return result;
    }

    public void setResult(ResponseDTO result) {
        this.result = result;
    }

    private ResponseDTO result;
    @Override
    public void run() {
        try {
            result = (ResponseDTO) this.executeCommand(dto);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
