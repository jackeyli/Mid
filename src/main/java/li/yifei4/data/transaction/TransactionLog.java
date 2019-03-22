package li.yifei4.data.transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TransactionLog {
    public static enum TransLog{
        INSTANCE;
        private TransactionLog log;
        TransLog(){
            log = new TransactionLog();
        }
        public TransactionLog getInstance(){
            return log;
        }
    }
    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    public ReentrantReadWriteLock.ReadLock rLock = readWriteLock.readLock();
    public ReentrantReadWriteLock.WriteLock wLock = readWriteLock.writeLock();
    public List<Transaction> processedTransaction = new ArrayList<Transaction>();
}
