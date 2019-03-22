package li.yifei4.service.course;

import li.yifei4.data.entity.scholarShip.MeritBasedScholarShip;
import li.yifei4.data.entity.scholarShip.ScholarShip;
import li.yifei4.data.entity.scholarShip.StatusBasedScholarShip;
import org.springframework.beans.BeanUtils;

public class ScholarshipFactory {
    public static ScholarShip getScholarship(String type,Object prototype){
        ScholarShip ship = null;
        switch(ScholarShip.ScholarShipType.valueOf(type)){
            case STATUSBASED:
                ship = new StatusBasedScholarShip();
                BeanUtils.copyProperties(prototype,ship);
                return ship;
            case MERITBASED:
                ship = new MeritBasedScholarShip();
                BeanUtils.copyProperties(prototype,ship);
                return ship;
        }
        return null;
    }
}
