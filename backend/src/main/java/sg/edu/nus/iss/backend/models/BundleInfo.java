package sg.edu.nus.iss.backend.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class BundleInfo {
    @Id
    @Field("_id")
    private String bundleId;
    private Date date;
    private String title;

    public BundleInfo() {
    }

    public BundleInfo(String bundleId, Date date, String title) {
        this.bundleId = bundleId;
        this.date = date;
        this.title = title;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public JsonObject toJson(){
        JsonObject jObj = Json.createObjectBuilder()
                            .add("bundleId", this.bundleId)
                            .add("date", this.date.getTime())
                            .add("title", this.title)
                            .build();
        return jObj;
    }
}
