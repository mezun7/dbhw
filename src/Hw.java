import com.mongodb.*;

import java.lang.reflect.Array;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by shuhrat on 25.03.14.
 */
public class Hw {
    public static void main(String[] args) throws UnknownHostException {
        double eps = 0.00000001;
        Mongo mongo = new Mongo("localhost", 27017);
        DB db = mongo.getDB("school");
        DBCollection collection = db.getCollection("students");
        DBCursor cursor = collection.find();

        while (cursor.hasNext()){
            boolean f = true;
            BasicDBObject element = (BasicDBObject) cursor.next();
            ArrayList<BasicDBObject> list  = (ArrayList<BasicDBObject>) element.get("scores");
            double min = 0;
            for (BasicDBObject i : list){
                if(i.get("type").equals("homework")) {
                    if(f) {
                        min = i.getDouble("score");
                        f = false;
                    } else if(i.getDouble("score") - min < eps) {
                        min = i.getDouble("score");
                    }

                }
            }
            int i = 0;
            int res = -1;
            while(i < list.size()) {
                if(list.get(i).get("type").equals("homework"))
                    if(Math.abs(list.get(i).getDouble("score") - min) < eps) {
                        res = i;
                    }
                i++;
            }
            list.remove(res);
            /*System.out.println(element.get("_id") + ":" + min);
            System.err.println(element);
            System.out.println("---------");
            */
            collection.update(new BasicDBObject("_id", element.get("_id")), element);
        }
    }
}
