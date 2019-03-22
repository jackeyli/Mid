package li.yifei4.data.entity.course;

import li.yifei4.data.entity.Entity;
import li.yifei4.data.entity.role.Student;
import li.yifei4.data.entity.role.User;
import li.yifei4.data.table.DataTable;
import li.yifei4.data.table.tableManager.DataManager;

import java.util.*;
import java.util.stream.Collectors;

public class RankBaseEvaluationRule extends EvaluationRule{

    private double aValue;
    private double bValue;
    public RankBaseEvaluationRule(){
        super(EvaluationRuleType.RANKBASED);
    }
    public DataTable retrieveRelatedTable(){
        return DataManager.getTable(EvaluationRule.class.getSimpleName());
    }
    public RankBaseEvaluationRule(Object ...obj){
        super(EvaluationRuleType.RANKBASED);
        this.aValue = (double)obj[0];
        this.bValue = (double)obj[1];
    }
    @Override
    public List<Grade> gradeCourse(Course course){
        List<Assignment> allAssn = Assignment.findByCourse(course.getId());
        List<CourseRegDocument> allRegStudents = CourseRegDocument.findByCourse(course.getId());
        List<AbstractMap.SimpleEntry<Student,Double>> scores = allRegStudents.parallelStream().map((t)->{
            Student cur = (Student) Entity.findById(User.class,t.getStudentId());
            return new AbstractMap.SimpleEntry<>(cur, allAssn.stream().mapToDouble((d) ->
                    d.getPoint() * Optional.ofNullable(AssignmentGrade.findByStudentAndAssignment(d.getId(), cur.getId()))
                            .map(p -> p.getScore()).orElse(0) / 100).sum());
        }).sorted((a,b)->b.getValue().compareTo(a.getValue())).collect(Collectors.toList());
        List<Grade> result = new ArrayList<Grade>();
        for(int i = 0; i < scores.size(); i ++){
            Grade nGrade = new Grade();
            nGrade.setStudentId(scores.get(i).getKey().getId());
            nGrade.setCourseId(course.getId());
            if(((double)(i + 1) / (double)scores.size()) <= aValue){
                nGrade.setGrade(Grade.GradeValue.A);
            }else if(((double)(i + 1) / (double)scores.size()) <= bValue){
                nGrade.setGrade(Grade.GradeValue.B);
            }else {
                nGrade.setGrade(Grade.GradeValue.C);
            }
            result.add(nGrade);
        }
        return result;
    }

    public double getaValue() {
        return aValue;
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

}
