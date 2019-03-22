package li.yifei4.data.transaction;

import li.yifei4.data.entity.Entity;
import li.yifei4.data.table.DataTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
public class Transaction extends Entity {
    public enum TransactionType{
        READ,WRITE
    }
    public enum DataOperationType{
        INSERT,UPDATE,DELETE
    }
    private List<DataTable> lockedTables = null;

    public HashMap<DataTable, DataTable> getRelatedTables() {
        return relatedTables;
    }

    private HashMap<DataTable, DataTable> relatedTables = new HashMap<DataTable,DataTable>();
    private Stack<DataOperation> processedOp = new Stack<DataOperation>();
    private TransactionType type;
    public Transaction(TransactionType tp){
        type = tp;
    }
    public static class DataOperation {
        private DataOperationType opType;
        private Entity opEntity;
        private Entity oldEntity;
        public DataOperation(DataOperationType opType,Entity opEntity){
            this.opType = opType;
            this.opEntity = opEntity;
        }
        public Entity getEntity(){
            return this.opEntity;
        }
        public DataOperationType getOpType(){
            return this.opType;
        }
        public void doOperation(){
            switch (this.getOpType()) {
                case INSERT:
                    this.getEntity().insertCommit();
                    break;
                case UPDATE:
                    this.oldEntity = this.getEntity().updateCommit();
                    break;
                case DELETE:
                    this.getEntity().deleteCommit();
                    break;
            }
        }
        public void reverseOperation(){
            switch (this.getOpType()) {
                case INSERT:
                    this.getEntity().deleteCommit();
                    break;
                case UPDATE:
                    this.oldEntity.updateCommit();
                    break;
                case DELETE:
                    this.getEntity().insertCommit();
                    break;
            }
        }
    }
    public void registRead(DataTable t ){
        this.relatedTables.put(t,t);
    }
    public void beginRead(){
        relatedTables.keySet().stream()
                .forEach((t)->{
                    t.lockRead();
                });
        lockedTables = relatedTables.keySet().stream().collect(Collectors.toList());
    }
    public void addOperation(DataOperation op){
        this.dataOps.add(op);
        this.relatedTables.put(op.getEntity().retrieveRelatedTable(),op.getEntity().retrieveRelatedTable());
    }
    public List<DataOperation> getDataOps() {
        return dataOps;
    }

    public boolean tryLockRelatedTbOp(){
        boolean isNeedRelease = true;
        try {
            lockedTables = relatedTables.keySet().stream().filter((t) -> {
                if (TransactionType.READ.equals(type))
                    return t.beginRead();
                else
                    return t.beginWrite();
            }).collect(Collectors.toList());
            if (lockedTables.size() < relatedTables.size()) {
                return false;
            } else {
                isNeedRelease = false;
                return true;
            }
        }finally {
            if(isNeedRelease && lockedTables != null){
                this.releaseLocks();
            }
        }
    }
    public void releaseLocks(){
        lockedTables.forEach((t)->{
            if(TransactionType.READ.equals(type))
                t.endRead();
            else
                t.endWrite();
        });
        lockedTables = null;
    }
    public void setDataOps(List<DataOperation> dataOps) {
        this.dataOps = dataOps;
    }
    public void rollback(){
       while(!this.processedOp.empty()){
           this.processedOp.pop().reverseOperation();
       }
    }
    public void commit(){
        while(!this.tryLockRelatedTbOp()){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        try {
            this.getDataOps().forEach((t)->{
                t.doOperation();
                this.processedOp.push(t);
            });
        }catch(Throwable e) {
            this.rollback();
            RuntimeException nRunTime = new RuntimeException(e.getMessage());
            nRunTime.setStackTrace(e.getStackTrace());
            throw nRunTime;
        } finally{
            this.releaseLocks();
        }
    }
    private List<DataOperation> dataOps = new ArrayList<DataOperation>();
}
