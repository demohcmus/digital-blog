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
public class ResCreateArticleDTO {
    private long id;
    private String title;
    private String content;
    private String createdBy;
    private Instant createdAt;
    
}
