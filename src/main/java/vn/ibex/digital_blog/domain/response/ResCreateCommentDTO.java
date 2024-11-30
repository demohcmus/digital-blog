package vn.ibex.digital_blog.domain.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResCreateCommentDTO {
    private long id;
    private String content;
    private Instant createdAt;
    
    private ArticleCMT article;    
    private UserCMT user;

    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArticleCMT {
        private long id;
        private String title;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserCMT {
        private long id;
        private String username;
    }
}
