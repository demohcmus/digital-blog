package vn.ibex.digital_blog.service.impl;

import java.util.Optional;


import org.springframework.stereotype.Service;

import vn.ibex.digital_blog.domain.Article;
import vn.ibex.digital_blog.domain.Comment;
import vn.ibex.digital_blog.domain.Role;
import vn.ibex.digital_blog.domain.User;
import vn.ibex.digital_blog.domain.response.ResCreateArticleDTO;
import vn.ibex.digital_blog.domain.response.ResCreateCommentDTO;
import vn.ibex.digital_blog.domain.response.ResUpdateCommentDTO;
import vn.ibex.digital_blog.repository.ArticleRepository;
import vn.ibex.digital_blog.repository.CommentRepository;
import vn.ibex.digital_blog.repository.RoleRepository;
import vn.ibex.digital_blog.repository.UserRepository;
import vn.ibex.digital_blog.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public CommentServiceImpl(ArticleRepository articleRepository,
            CommentRepository commentRepository,
            UserRepository userRepository,
            RoleRepository roleRepository) {
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    // Save Comment to Article
    public ResCreateCommentDTO saveComment(String content, long articleId, String email) {

        User user = this.userRepository.findByEmail(email);
        // if user is null, set role to ANONYMOUS
        
        
        Article article = this.articleRepository.findById(articleId).get();
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setArticle(article);
        if(user!=null)
        comment.setUser(user);

        comment = this.commentRepository.save(comment);

        ResCreateCommentDTO resCreateCommentDTO = new ResCreateCommentDTO(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt()
        );
        ResCreateCommentDTO.ArticleCMT articleCMT = new ResCreateCommentDTO.ArticleCMT(
            article.getId(),
            article.getTitle()
        );
        ResCreateCommentDTO.UserCMT userCMT;
        if(user!=null){
            userCMT= new ResCreateCommentDTO.UserCMT(
                user.getId(),
                user.getEmail()
            );
        }else{
            userCMT= new ResCreateCommentDTO.UserCMT(
                3,
                "Anonymous"
            );
        }


        resCreateCommentDTO.setArticle(articleCMT);
        resCreateCommentDTO.setUser(userCMT);
        return resCreateCommentDTO;

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

            ResUpdateCommentDTO resUpdateCommentDTO = new ResUpdateCommentDTO(
                    comment.getId(),
                    comment.getContent(),
                    comment.getUpdatedAt()
            );
            ResUpdateCommentDTO.ArticleCMT articleCMT = new ResUpdateCommentDTO.ArticleCMT(
                currentArticle.get().getId(),
                currentArticle.get().getTitle()
            );
            ResUpdateCommentDTO.UserCMT userCMT = new ResUpdateCommentDTO.UserCMT(
                currentUser.get().getId(),
                currentUser.get().getEmail()
            );



            resUpdateCommentDTO.setArticle(articleCMT);
            resUpdateCommentDTO.setUser(userCMT);
            return resUpdateCommentDTO;
        }
        return null;

    }
}