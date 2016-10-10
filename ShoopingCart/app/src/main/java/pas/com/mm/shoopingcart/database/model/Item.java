package pas.com.mm.shoopingcart.database.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by phyo on 08/10/2016.
 */
@IgnoreExtraProperties
public class Item {

    public String code;
    public String description;
    public String imgUrl;
    public String getCode() {
        return code;
    }

    public Item() {
    }

    public Item(String code, String description, String imgUrl, Double amount) {
        this.code = code;
        this.description = description;
        this.imgUrl = imgUrl;
        this.amount = amount;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    private Double amount;


    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("amount", amount);
        result.put("code", code);
        result.put("description", description);
        result.put("imgUrl", imgUrl);


        return result;
    }
    // [END post_to_map]
}
