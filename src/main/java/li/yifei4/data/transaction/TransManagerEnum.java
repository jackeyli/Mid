package li.yifei4.data.transaction;

public enum TransManagerEnum{
    INSTANCE;
    private TransactionManager manager;
    TransManagerEnum(){
        manager = new TransactionManager();
    }
    public TransactionManager getInstance(){
        return manager;
    }
}
