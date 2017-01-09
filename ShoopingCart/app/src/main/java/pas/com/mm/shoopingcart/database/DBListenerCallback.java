package pas.com.mm.shoopingcart.database;

import java.util.List;

import pas.com.mm.shoopingcart.database.model.Model;

/**
 * Created by phyoa on 10/23/2016.
 */

public interface DBListenerCallback  {
    public void LoadCompleted(boolean b);
    public void receiveResult(Model model);
}
