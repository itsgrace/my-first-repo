package cs.dal.food4fit;

import android.app.Application;
import android.content.Context;

/**
 * Created by graceliu on 2017-11-28.
 */

public class toolfunction extends Application {


    public static Context context;

    @Override public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

}
