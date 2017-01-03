package pas.com.mm.shoopingcart.util;

import java.util.ArrayList;
import java.util.List;

import pas.com.mm.shoopingcart.database.model.Item;

/**
 * Created by phyo on 30/12/2016.
 */

public class Sorter {

    public static List<Item> setRecentFirst(List<Item> list, String code)
    {
        if(code==null || code.equals(""))
        {
            return list;
        }
        List<Item> single=new ArrayList<Item>();
          for(Item i:list)
          {
             if(i.getCode().equals(code))
             {
                 list.remove(i);
                 single.add(i);
                 break;
             }
          }
        single.addAll(list);
        return single;
    }
}
