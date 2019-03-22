package li.yifei4.data.entity.course;

import li.yifei4.data.entity.Entity;
import li.yifei4.data.entity.role.Student;
import li.yifei4.data.entity.role.User;
import li.yifei4.data.table.DataTable;
import li.yifei4.data.table.tableManager.DataManager;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PointsBaseEvaluationRule extends EvaluationRule{
    private double aValue;
    private double bValue;
    public PointsBaseEvaluationRule(){
        super(EvaluationRuleType.POINTBASED);
    }
    public PointsBaseEvaluationRule(Object ...obj){
        super(EvaluationRuleType.POINTBASED);
        this.aValue = (double)obj[0];
        this.bValue = (double)obj[1];
    }
    public double getaValue() {
        return aValue;
    }
    public DataTable retrieveRelatedTable(){
        return DataManager.getTable(EvaluationRule.class.getSimpleName());
    }
    public void setaValue(double aValue) {
        this.aValue = aValue;
    }

    public double getbValue() {
        return bValue;
    }

    public void setbValue(double bValue) {
        this.bValue = bValue;
    }

    @Override
    public List<Grade> gradeCourse(Course course){
        List<Assignment> allAssn = Assignment.findByCourse(course.getId());
        List<CourseRegDocument> allRegStudents = CourseRegDocument.findByCourse(course.getId());
        return allRegStudents.parallelStream().map((t)->{
            Student cur = (Student) Entity.findById(User.class,t.getStudentId());
            int score = (int) allAssn.stream().mapToDouble((d) ->
                    d.getPoint() * Optional.ofNullable(AssignmentGrade.findByStudentAndAssignment(d.getId(), cur.getId()))
                            .map(p -> p.getScore()).orElse(0) / 100).sum();
            Grade nGrade = new Grade();
            nGrade.setCourseId(course.getId());
            nGrade.setStudentId(cur.getId());
            if(score >= aValue)
                nGrade.setGrade(Grade.GradeValue.A);
            else if(score >= bValue)
                nGrade.setGrade(Grade.GradeValue.B);
            else
                nGrade.setGrade(Grade.GradeValue.C);
            return nGrade;
        }).collect(Collectors.toList());
    }
}
