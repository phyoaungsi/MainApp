package pas.com.mm.shoopingcart.database.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by phyo on 08/01/2017.
 */
@IgnoreExtraProperties
public class Config extends Model {

    public boolean getPromotionOn() {
        return promotionOn;
    }

    public void setPromotionOn(boolean promotionOn) {
        this.promotionOn = promotionOn;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMessageNumber() {
        return messageNumber;
    }

    public void setMessageNumber(String messageNumber) {
        this.messageNumber = messageNumber;
    }

    public boolean getOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getPromotionBanner() {
        return promotionBanner;
    }

    public void setPromotionBanner(String promotionBanner) {
        this.promotionBanner = promotionBanner;
    }

    public String getPromotionImage() {
        return promotionImage;
    }

    public void setPromotionImage(String promotionImage) {
        this.promotionImage = promotionImage;
    }

    public String phoneNumber;
    public String messageNumber;
    public String promotionBanner;
    public String promotionImage;
    public String address1;



    public String address2;
    public String getSmsMessage() {
        return smsMessage;
    }

    public void setSmsMessage(String smsMessage) {
        this.smsMessage = smsMessage;
    }

    public String smsMessage;
    public Boolean promotionOn,open;

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }
}
