package pas.com.mm.shoopingcart.common;

import pas.com.mm.shoopingcart.util.FontOverride;

/**
 * Created by phyo on 22/12/2016.
 */
public final class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        boolean unicode=true;
        if(unicode) {
            FontOverride.setDefaultFont(this, "DEFAULT", "fonts/font.ttf");
            FontOverride.setDefaultFont(this, "MONOSPACE", "fonts/font.ttf");
            FontOverride.setDefaultFont(this, "SERIF", "fonts/font.ttf");
            FontOverride.setDefaultFont(this, "SANS_SERIF", "fonts/font.ttf");
        }
        else
        {
            FontOverride.setDefaultFont(this, "DEFAULT", "fonts/zawgyi.ttf");
            FontOverride.setDefaultFont(this, "MONOSPACE", "fonts/zawgyi.ttf");
            FontOverride.setDefaultFont(this, "SERIF", "fonts/zawgyi.ttf");
            FontOverride.setDefaultFont(this, "SANS_SERIF", "fonts/zawgyi.ttf");
        }
    }
}