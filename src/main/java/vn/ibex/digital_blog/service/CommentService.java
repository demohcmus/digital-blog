package vn.ibex.digital_blog.service;

import vn.ibex.digital_blog.domain.response.ResCreateCommentDTO;
import vn.ibex.digital_blog.domain.response.ResUpdateCommentDTO;

public interface CommentService {
    ResCreateCommentDTO saveComment(String content, long articleId, String email);
    ResUpdateCommentDTO updateComment(long commentId, String content);
}
