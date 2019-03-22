package li.yifei4.data.transaction;

public class TransactionManager {
    private ThreadLocal<Transaction> rLocal = new ThreadLocal<Transaction>();
    private ThreadLocal<Transaction> wLocal = new ThreadLocal<Transaction>();
    public void clear(){
        rLocal.set(null);
        wLocal.set(null);
    }
    public static TransactionManager getInstance(){
        return TransManagerEnum.INSTANCE.getInstance();
    }
    public Transaction createReadTransaction(){
        rLocal.set(new Transaction(Transaction.TransactionType.READ));
        return rLocal.get();
    }
    public void beginWriteTransaction(){
        wLocal.set(new Transaction(Transaction.TransactionType.WRITE));
    }
    public void commit(){
        if(rLocal.get() != null)
            rLocal.get().releaseLocks();
        if(wLocal.get() != null) {
            wLocal.get().commit();
            TransactionLog.TransLog.INSTANCE.getInstance().wLock.lock();
            try {
                TransactionLog.TransLog.INSTANCE.getInstance().processedTransaction.add(wLocal.get());
            } finally {
                TransactionLog.TransLog.INSTANCE.getInstance().wLock.unlock();
            }
        }
    }
    public Transaction getReadTransaction(){
        return rLocal.get();
    }
    public Transaction getWriteTransaction(){
        return wLocal.get();
    }
}
