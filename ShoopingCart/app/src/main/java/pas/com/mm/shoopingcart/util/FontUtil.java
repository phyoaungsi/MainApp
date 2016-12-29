package pas.com.mm.shoopingcart.util;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by phyo on 29/12/2016.
 */

public class FontUtil {

    public static void setZawGyiText(Context context, TextView textView,String text)
    {
        Typeface typeface=Typeface.createFromAsset(context.getAssets(), "fonts/zawgyi.ttf");
        textView.setTypeface(typeface);
        textView.setText(text);
    }
}
