package tests.DTO;

public class CommentDTO {

    private int userId;
    public int getUserId() {
        return userId;
    }
    public CommentDTO setUserId(int userId) {
        this.userId = userId;
        return this;
    }
    public String getId() {
        return id;
    }
    public CommentDTO setId(String entryId) {
        this.id = entryId;
        return this;
    }
    public String getTitle() {
        return title;
    }
    public CommentDTO setTitle(String entryTitle) {
        this.title = entryTitle;
        return this;
    }
    public String getBody() {
        return body;
    }
    public CommentDTO setBody(String body) {
        this.body = body;
        return this;
    }
    private String id;
    private String title;
    private String body;

}