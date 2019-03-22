package li.yifei4.data.entity.course;

import li.yifei4.data.entity.Entity;
import li.yifei4.service.course.GradeStrategy;

public abstract class EvaluationRule extends Entity implements GradeStrategy {
    public EvaluationRuleType getRuleType() {
        return ruleType;
    }
    public EvaluationRule(){

    }
    public EvaluationRule(EvaluationRuleType type){
        this.ruleType = type;
    }
    public void setRuleType(EvaluationRuleType ruleType) {
        this.ruleType = ruleType;
    }

    public enum EvaluationRuleType{
        POINTBASED,RANKBASED;
    }
    private EvaluationRuleType ruleType = null;
}
