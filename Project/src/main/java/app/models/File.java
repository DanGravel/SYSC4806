package app.models;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<User> user;
    private String fileName;
    private String fileType;
    private String status;
    private Date date;

    @Lob
    private byte[] data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setStatus(String status) { this.status = status;}

    public String getStatus() { return this.status;}

    public void setDate(Date date) {this.date = date;}

    public Date getDate() {return this.date;}

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof File)) return false;
        File f = (File) obj;
        return f.id == this.id && f.fileName == this.fileName && f.fileType == this.fileType;
    }
}
