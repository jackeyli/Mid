package li.yifei4.data.entity.course;

import li.yifei4.data.entity.Entity;
import li.yifei4.data.table.tableManager.DataManager;

import java.util.List;
import java.util.stream.Collectors;

public class Assignment extends Entity {
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public enum AssignmentType{
        ESSAY,MULTIPLECHOICE
    }
    private AssignmentType assignType;
    public Assignment.AssignmentType getAssignType() {
        return assignType;
    }

    public void setAssignType(Assignment.AssignmentType assignType) {
        this.assignType = assignType;
    }

    private String name;
    private long courseId = -1;
    private String publishDate;
    private String dueDate;
    private String content;
    private int point;

    public static List<Assignment> findByCourse(long courseId){
        return (List<Assignment>) DataManager.getTable(Assignment.class.getSimpleName())
                .getTableData().stream().filter((t)->((Assignment)t).getCourseId() == courseId)
                .collect(Collectors.toList());
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}
