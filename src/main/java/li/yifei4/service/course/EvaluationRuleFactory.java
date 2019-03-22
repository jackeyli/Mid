package li.yifei4.service.course;

import li.yifei4.data.entity.course.EvaluationRule;
import li.yifei4.data.entity.course.PointsBaseEvaluationRule;
import li.yifei4.data.entity.course.RankBaseEvaluationRule;

public class EvaluationRuleFactory {
    public static EvaluationRule getEvaluationRule(String type,Object ...obj){
        switch(EvaluationRule.EvaluationRuleType.valueOf(type)){
            case POINTBASED:
                return new PointsBaseEvaluationRule(obj);
            case RANKBASED:
                return new RankBaseEvaluationRule(obj);
        }
        return null;
    }
}
