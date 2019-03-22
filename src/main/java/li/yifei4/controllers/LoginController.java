package li.yifei4.controllers;

import li.yifei4.ResponseDTO;
import li.yifei4.data.entity.role.User;
import li.yifei4.data.table.tableManager.DataManager;
import li.yifei4.data.transaction.Transaction;
import li.yifei4.data.transaction.TransactionManager;
import li.yifei4.service.course.UserFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.Objects;

@Controller
@RequestMapping("/login")
public class LoginController {
    @RequestMapping("/login")
    @ResponseBody
    public ResponseDTO Login(String userId, String password){
        ResponseDTO resp = new ResponseDTO();
        Transaction readTransaction = TransactionManager.getInstance().createReadTransaction();
        readTransaction.registRead(DataManager.getTable(User.class.getSimpleName()));
        readTransaction.beginRead();
        try {
            HttpSession session = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
            if(session.getAttribute("user") != null){
                resp.setValue(session.getAttribute("user"));
            } else {
                User user = (User) DataManager.getTable(User.class.getSimpleName()).getTableData().stream()
                        .filter((t)-> Objects.equals(((User)t).getName(),userId) &&
                                Objects.equals(((User)t).getPassword(),password)).findAny().orElse(null);
                if (user == null) {
                    resp.setErrorMessage("Invalid User Name or Password");
                } else {
                    session.setAttribute("user", user);
                    resp.setValue(user);
                }
            }
        }finally{
            try {
                TransactionManager.getInstance().commit();
            }catch(Throwable e){
                resp.setErrorMessage(e.getMessage());
            }
            return resp;
        }
    }

    @RequestMapping("/logout")
    @ResponseBody
    public ResponseDTO LogOut(){
        ResponseDTO resp = new ResponseDTO();
        HttpSession session = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        session.setAttribute("user",null);
        resp.setValue("Done!");
        return resp;
    }
    @RequestMapping("/regist")
    @ResponseBody
    public ResponseDTO Regist(@RequestParam String userId,@RequestParam String password,@RequestParam String type){
        ResponseDTO resp = new ResponseDTO();
        TransactionManager.getInstance().beginWriteTransaction();
        try {
            User user = UserFactory.getUser(type,userId,password);
            HttpSession session = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
            session.setAttribute("user",user);
            user.insert();
            resp.setValue(user);
        }finally{
            try {
                TransactionManager.getInstance().commit();
            }catch(Throwable e){
                resp.setErrorMessage(e.getMessage());
            }
            return resp;
        }
    }
}
