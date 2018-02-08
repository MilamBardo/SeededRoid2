package extensions;

import java.util.ArrayList;

import db.MySQLiteHelper;
import models.SeedType;

/**
 * Created by Talanath on 5/27/2017.
 */

public class ApplicationOverride extends android.app.Application  {

        private ArrayList<SeedType> seedTypes;

        public ArrayList<SeedType> getSeedTypes() {
            if (seedTypes == null)
            {
                MySQLiteHelper database = new MySQLiteHelper(this.getApplicationContext());

            }
            return seedTypes;
        }

//        public void setGlobalVarValue(String str) {
//            seedTypes = str;
//        }

}
