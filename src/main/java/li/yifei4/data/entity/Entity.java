package li.yifei4.data.entity;

import li.yifei4.data.exceptions.ConcurrentException;
import li.yifei4.data.sequence.Sequence;
import li.yifei4.data.sequence.SequencePool;
import li.yifei4.data.table.DataTable;
import li.yifei4.data.table.tableManager.DataManager;
import li.yifei4.data.transaction.TransManagerEnum;
import li.yifei4.data.transaction.Transaction;
import org.springframework.beans.BeanUtils;

import java.util.Objects;

public abstract class Entity {
    private long id = -1;
    private long version;
    private String actualClass = null;
    public Sequence retrieveSequence(){
        return SequencePool.get(this.retrieveRelatedTable().getTableName());
    }
    public Entity findAny(){
        return (Entity) this.retrieveRelatedTable().getTableData().stream()
                .filter((t)->Objects.equals((t).getClass(),this.getClass())).findAny().orElse(null);
    }
    public void genID(){
        this.id = this.retrieveSequence().curVal();
    }
    public long getId(){
        return id;
    }
    public DataTable retrieveRelatedTable(){
        return DataManager.getTable(this.getClass().getSimpleName());
    }
    public static Entity findById(Class<? extends Entity>clz, long id){
        return (Entity) DataManager.getTable(clz.getSimpleName())
                .getTableData().stream()
                .filter(t -> Objects.equals(id, ((Entity) t).getId()))
                .findAny().orElse(null);
    }
    public void insert(){
        if(this.getId() <= 0) {
            this.genID();
            this.actualClass = this.getClass().getName();
        }
        TransManagerEnum.INSTANCE
                .getInstance()
                .getWriteTransaction().addOperation(new Transaction.DataOperation(Transaction.DataOperationType.INSERT,this));
    }
    public void update(){
        if(this.actualClass == null)
            this.actualClass = this.getClass().getName();
        TransManagerEnum.INSTANCE
                .getInstance()
                .getWriteTransaction().addOperation(new Transaction.DataOperation(Transaction.DataOperationType.UPDATE,this));
    }
    public void delete(){
        TransManagerEnum.INSTANCE
                .getInstance()
                .getWriteTransaction().addOperation(new Transaction.DataOperation(Transaction.DataOperationType.DELETE,this));
    }
    public void insertCommit(){
        this.retrieveRelatedTable()
                .getTableData().add(this);
    }
    public Entity updateCommit(){
        try {
            Entity thisEntity = (Entity) this.retrieveRelatedTable()
                    .getTableData().stream()
                    .filter(t -> Objects.equals(this.getId(), ((Entity) t).getId()))
                    .findAny().orElse(null);
            Entity backUp = this.getClass().newInstance();
            if (this.getVersion() < thisEntity.getVersion()) {
                throw new ConcurrentException();
            }
            this.setVersion(this.getVersion() + 1);
            BeanUtils.copyProperties(thisEntity, backUp);
            BeanUtils.copyProperties(this, thisEntity);
            return backUp;
        }catch(Throwable e){
            if(e instanceof  RuntimeException) {
                throw (RuntimeException)e;
            }
            else {
                RuntimeException runtime = new RuntimeException();
                runtime.setStackTrace(e.getStackTrace());
                throw runtime;
            }
        }
    }
    public void deleteCommit(){
        this.retrieveRelatedTable()
                .getTableData().removeIf((t)->{
                    if(Objects.equals(((Entity)t).getId(),this.getId())){
                        if(this.getVersion() < ((Entity)t).getVersion()) {
                            throw new ConcurrentException();
                        }
                        return true;
                    }
                    return false;
                   });
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getActualClass() {
        return actualClass;
    }

    public void setActualClass(String actualClass) {
        this.actualClass = actualClass;
    }
}
