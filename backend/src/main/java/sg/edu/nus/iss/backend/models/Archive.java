package sg.edu.nus.iss.backend.models;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

public class Archive {

    @Id
    @Field("_id")
    private String bundleId;
    private String name;
    private Date date;
    private String title;
    private String comments;
    private List<String> urls;

    public Archive() {
    }

    public Archive(String bundleId, String name, Date date, String title, String comments, List<String> url) {
        this.bundleId = bundleId;
        this.name = name;
        this.date = date;
        this.title = title;
        this.comments = comments;
        this.urls = url;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> url) {
        this.urls = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
