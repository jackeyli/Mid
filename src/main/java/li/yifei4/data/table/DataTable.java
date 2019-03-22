package li.yifei4.data.table;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import li.yifei4.SystemProperties;
import li.yifei4.data.entity.Entity;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DataTable{
    private String tableName = null;
    public List getTableData() {
        return tableData;
    }
    public void setTableData(List tableData) {
        this.tableData = tableData;
    }
    private  List tableData = new ArrayList();
    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock rLock = readWriteLock.readLock();
    private ReentrantReadWriteLock.WriteLock wLock = readWriteLock.writeLock();
    public void lockRead(){
        rLock.lock();
    }
    public boolean beginRead(){
        return rLock.tryLock();
    }
    public void endRead(){
        rLock.unlock();
    }
    public boolean beginWrite(){
        return wLock.tryLock();
    }
    public void endWrite(){
        wLock.unlock();
    }
    public void Flush() throws IOException
    {
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(this.tableData);
        File originFile = new File(SystemProperties.DATA_FOLDER + "/" + this.tableName + ".data");
        if(!originFile.exists())
            originFile.createNewFile();
        originFile.renameTo(new File(SystemProperties.DATA_FOLDER + "/" + this.tableName + ".data.back"));
        originFile = new File(SystemProperties.DATA_FOLDER + "/" + this.tableName + ".data.back");
        File flushFile =  new File(SystemProperties.DATA_FOLDER + "/" + this.tableName + ".data");
        if(!flushFile.exists()){
            flushFile.createNewFile();
        }
        RandomAccessFile rfile = new RandomAccessFile(flushFile,"rw");
        try {
            rfile.writeUTF(json);
            originFile.delete();
        }catch(Throwable e){
            if(originFile.exists()) {
                originFile.renameTo(new File(SystemProperties.DATA_FOLDER + "/" + this.tableName + ".data"));
            }
            throw e;
        }finally{
            rfile.close();
        }
    }
    public String getTableName() {
        return tableName;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
