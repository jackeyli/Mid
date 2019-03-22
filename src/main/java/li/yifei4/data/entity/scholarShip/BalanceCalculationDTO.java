package li.yifei4.data.entity.scholarShip;

import li.yifei4.data.entity.role.Student;

public class BalanceCalculationDTO {
    private Student student;
    private double balance;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
