package pas.com.mm.shoopingcart.util;

import pas.com.mm.shoopingcart.common.ApplicationConfig;
import pas.com.mm.shoopingcart.database.model.Item;

/**
 * Created by phyo on 09/01/2017.
 */

public class StringUtils {


    public static String getSmsMessage(Item item){

        String message= ApplicationConfig.smsMessage;

        if (message==null || message.equals(""))
        {
            return "I want to buy "+item.getTitle()+" [CODE:"+item.getCode()+"]";
        }
        message=message.replace("${code}",item.getCode());
        message=message.replace("${title}",item.getTitle());
        message=message.replace("${amount}",Double.toString(item.getAmount()));
        message=message.replace("${image}",item.getImgUrl());
        return message;

    }
}
