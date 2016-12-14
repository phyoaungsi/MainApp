package pas.com.mm.shoopingcart.database.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by phyo on 12/12/2016.
 */
@IgnoreExtraProperties
public class NotificationModel {


    public String notificationId;
    public String message;
    public String title;


    public String uuid;


    public String content;
    public String type;
    public String bannerImage;
    public String mainImage;
    public String html;
    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("notificationId", notificationId);
        result.put("message", message);
        result.put("title", title);
        result.put("uuid", uuid);
        result.put("content", content);
        result.put("type", type);
        result.put("bannerImage", bannerImage);
        result.put("mainImage", mainImage);
        result.put("html", html);

        return result;
    }

}
