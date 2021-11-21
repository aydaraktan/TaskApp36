package kg.geektech.taskapp36.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
@Entity
public class Task implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String docId;
    private String text;
    private long createdAt;
    private String imageUrl;

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Task(String text, long createdAt, String imageUrl) {
        this.text = text;
        this.createdAt = createdAt;
        this.imageUrl = imageUrl;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public Task() {
    }

    public Task(String text, long createdAt) {
        this.text = text;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
