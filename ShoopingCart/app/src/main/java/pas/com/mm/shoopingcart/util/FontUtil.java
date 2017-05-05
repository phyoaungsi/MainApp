package pas.com.mm.shoopingcart.util;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

    public static void setFont(Context context, TextView textView)
    {

        Typeface typeface=Typeface.createFromAsset(context.getAssets(), "fonts/zawgyi.ttf");
        textView.setTypeface(typeface);

    }

    public static void setUniText(Context context, TextView textView,String text)
    {
        Typeface typeface=Typeface.createFromAsset(context.getAssets(), "fonts/font.ttf");
        textView.setTypeface(typeface);
        textView.setText(text);
    }

    public static void setText(Context context, Toolbar toolbar, boolean isUnicode)
    {
        Typeface typeface=Typeface.createFromAsset(context.getAssets(), "fonts/font.ttf");
        if(!isUnicode) {
            typeface=Typeface.createFromAsset(context.getAssets(), "fonts/zawgyi.ttf");
        }
        for(int i = 0; i < toolbar.getChildCount(); i++){
            View view = toolbar.getChildAt(i);
            if(view instanceof TextView){
                TextView tv = (TextView) view;

                    tv.setTypeface(typeface);
                   break;
                }
            }
        }


}
