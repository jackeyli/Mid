package li.yifei4.data.threads;

import li.yifei4.SystemProperties;
import li.yifei4.data.sequence.SequencePool;
import li.yifei4.data.table.DataTable;
import li.yifei4.data.transaction.TransactionLog;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


public class FlushDataJob implements Runnable{
    private List<DataTable> flushTables = null;
    private boolean tryGetLock(){
        TransactionLog.TransLog.INSTANCE.getInstance().wLock.lock();
        boolean isNeedReleaseLock = true;
        try{
            HashMap<DataTable,DataTable> relatedTables = new HashMap<DataTable,DataTable>();
            TransactionLog.TransLog.INSTANCE.getInstance()
                    .processedTransaction.stream().forEach((t)->{
                        t.getRelatedTables().values().forEach((p)->{
                            relatedTables.put(p,p);
                        });
            });
            List<DataTable> lockedTables = flushTables = relatedTables.values().stream().filter((t)->{
                return t.beginRead();
            }).collect(Collectors.toList());
            if(lockedTables.size() < relatedTables.values().size()){
                return false;
            } else {
                TransactionLog.TransLog.INSTANCE.getInstance()
                        .processedTransaction.clear();
                isNeedReleaseLock = false;
                return true;
            }
        }finally{
            TransactionLog.TransLog.INSTANCE.getInstance().wLock.unlock();
            if(isNeedReleaseLock && flushTables != null){
               this.releaseLock();
            }
        }
    }
    private void releaseLock(){
        flushTables.forEach((t)->{
            t.endRead();
        });
        flushTables = null;
    }
    private void flush() throws IOException {
        while(!tryGetLock()){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        try {
            for (DataTable flushTable : flushTables) {
                try {
                    flushTable.Flush();
                    SequencePool.get(flushTable.getTableName()).Flush();
                }catch(Throwable e){

                }
            }
        }finally{
            this.releaseLock();
        }
    }
    @Override
    public void run() {
            while(true) {
                try {
                    Thread.sleep(6000);
                    flush();
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
    }
}
