package li.yifei4.data.sequence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import li.yifei4.SystemProperties;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.atomic.AtomicLong;

public class Sequence {
    private AtomicLong curVal = null;
    private String name = null;
    public Sequence(long initV,String name){
        this.curVal = new AtomicLong(initV);
        this.name = name;
    }
    public long curVal(){
        return this.curVal.getAndIncrement();
    }
    public void Flush() throws IOException {
        RandomAccessFile rfile = new RandomAccessFile(SystemProperties.DATA_FOLDER + "/Sequences/" + this.getName() + ".seq","rw");
        rfile.writeLong(this.curVal());
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
