package li.yifei4.data.table.tableManager;

import com.google.gson.*;
import li.yifei4.SystemProperties;
import li.yifei4.data.entity.course.*;
import li.yifei4.data.entity.role.User;
import li.yifei4.data.entity.scholarShip.CreditRate;
import li.yifei4.data.entity.scholarShip.ScholarShip;
import li.yifei4.data.entity.scholarShip.ScholarShipAssignment;
import li.yifei4.data.sequence.Sequence;
import li.yifei4.data.sequence.SequencePool;
import li.yifei4.data.table.DataTable;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
public class DataManager {
    /**
     * Mostly Read
     */
    public static HashMap<String, DataTable> tables = new HashMap<String,DataTable>();
    public static DataTable getTable(String tableName){
        return tables.get(tableName);
    }
    public static void InitDataTable() throws IOException, ClassNotFoundException {
        String folder = SystemProperties.DATA_FOLDER;
        if(!Files.exists(Paths.get(SystemProperties.DATA_FOLDER))) {
            Files.createDirectory(
                    Paths.get(SystemProperties.DATA_FOLDER));
        }
        if((!Files.exists(Paths.get(SystemProperties.DATA_FOLDER + "/Sequences")))){
            Files.createDirectory(Paths.get(SystemProperties.DATA_FOLDER + "/Sequences"));
        }
        String[] dataTables = new String[]{User.class.getSimpleName(),ScholarShip.class.getSimpleName(),
                ScholarShipAssignment.class.getSimpleName(), Semester.class.getSimpleName(),
                Grade.class.getSimpleName(), EvaluationRule.class.getSimpleName(),
                CourseRegDocument.class.getSimpleName(),Course.class.getSimpleName(),
                AssignmentSubmission.class.getSimpleName(),AssignmentGrade.class.getSimpleName(),
                Assignment.class.getSimpleName(), CreditRate.class.getSimpleName()};
        JsonParser parser = new JsonParser();
        Gson gson = new Gson();
        for(int i = 0; i < dataTables.length; i ++){
            String name = dataTables[i];
            File file= new File(SystemProperties.DATA_FOLDER + "/Sequences/" + name + ".seq");
            if(file.exists()){
                FileInputStream fis = new FileInputStream(file);
                DataInputStream dis = new DataInputStream(fis);
                long val = dis.readLong();
                SequencePool.add(new Sequence(val,name));
                fis.close();
                dis.close();
            } else {
                SequencePool.add(new Sequence(1,name));
            }
            File dataFile = new File(SystemProperties.DATA_FOLDER + "/" + name + ".data");
            DataTable thisTable  = new DataTable();
            thisTable.setTableName(name);
            if(dataFile.exists()){
                FileInputStream fis = new FileInputStream(dataFile);
                DataInputStream dis = new DataInputStream(fis);
                String totalData = dis.readUTF();
                JsonArray jsonArray = parser.parse(totalData).getAsJsonArray();
                for (JsonElement ele : jsonArray) {
                    Class thisClass = (Class) Class.forName(((JsonObject) ele).get("actualClass").getAsString());
                    Object obj = gson.fromJson(ele,thisClass);
                    thisTable.getTableData().add(obj);
                }
                fis.close();
                dis.close();
            }
            DataManager.tables.put(name,thisTable);
        }
    }
}
