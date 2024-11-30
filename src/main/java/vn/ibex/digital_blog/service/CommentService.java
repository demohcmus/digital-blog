package vn.ibex.digital_blog.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.ibex.digital_blog.domain.Article;
import vn.ibex.digital_blog.domain.Comment;
import vn.ibex.digital_blog.domain.User;
import vn.ibex.digital_blog.domain.response.ResCreateCommentDTO;
import vn.ibex.digital_blog.domain.response.ResUpdateCommentDTO;
import vn.ibex.digital_blog.repository.ArticleRepository;
import vn.ibex.digital_blog.repository.CommentRepository;
import vn.ibex.digital_blog.repository.UserRepository;

@Service
public class CommentService {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    public CommentService(ArticleRepository articleRepository,
     CommentRepository commentRepository,
     UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    // Save Comment to Article
    public ResCreateCommentDTO saveComment(long articleId, long userId, String content) {
        Optional<Article> currentArticle = this.articleRepository.findById(articleId);
        if (currentArticle.isPresent()) {
            Comment comment = new Comment();
            Optional<User> currentUser = this.userRepository.findById(userId);
            comment.setArticle(currentArticle.get());
            comment.setContent(content);
            comment.setUser(currentUser.get());
            comment = this.commentRepository.save(comment);

            ResCreateCommentDTO resCreateCommentDTO = new ResCreateCommentDTO();
            ResCreateCommentDTO.ArticleCMT articleCMT = new ResCreateCommentDTO.ArticleCMT();
            ResCreateCommentDTO.UserCMT userCMT = new ResCreateCommentDTO.UserCMT();
            resCreateCommentDTO.setId(comment.getId());
            resCreateCommentDTO.setContent(comment.getContent());
            resCreateCommentDTO.setCreatedAt(comment.getCreatedAt());
            
            articleCMT.setId(currentArticle.get().getId());
            articleCMT.setTitle(currentArticle.get().getTitle());

            userCMT.setId(currentUser.get().getId());
            userCMT.setUsername(currentUser.get().getEmail());

            resCreateCommentDTO.setArticle(articleCMT);
            resCreateCommentDTO.setUser(userCMT);
            return resCreateCommentDTO;
        }
        return null;



    }


     // Update Comment to Article
     public ResUpdateCommentDTO updateComment(long commentId, String content) {
        Optional<Comment> currentComment = this.commentRepository.findById(commentId);
        if (currentComment.isPresent() && content != null) {
            Comment comment = currentComment.get();
            Optional<Article> currentArticle = this.articleRepository.findById(comment.getArticle().getId());
            Optional<User> currentUser = this.userRepository.findById(comment.getUser().getId());
            comment.setContent(content);
            this.commentRepository.save(comment);

            ResUpdateCommentDTO resUpdateCommentDTO = new ResUpdateCommentDTO();
            ResUpdateCommentDTO.ArticleCMT articleCMT = new ResUpdateCommentDTO.ArticleCMT();
            ResUpdateCommentDTO.UserCMT userCMT = new ResUpdateCommentDTO.UserCMT();
            resUpdateCommentDTO.setId(comment.getId());
            resUpdateCommentDTO.setContent(comment.getContent());
            resUpdateCommentDTO.setUpdatedAt(comment.getUpdatedAt());
            
            articleCMT.setId(currentArticle.get().getId());
            articleCMT.setTitle(currentArticle.get().getTitle());

            userCMT.setId(currentUser.get().getId());
            userCMT.setUsername(currentUser.get().getEmail());

            resUpdateCommentDTO.setArticle(articleCMT);
            resUpdateCommentDTO.setUser(userCMT);
            return resUpdateCommentDTO;
        }
        return null;
        


    }
}