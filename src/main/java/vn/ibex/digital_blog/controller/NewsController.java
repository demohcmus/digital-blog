package vn.ibex.digital_blog.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.ibex.digital_blog.domain.Article;
import vn.ibex.digital_blog.domain.Comment;
import vn.ibex.digital_blog.domain.response.ResCreateArticleDTO;
import vn.ibex.digital_blog.domain.response.ResCreateCommentDTO;
import vn.ibex.digital_blog.domain.response.ResUpdateArticleDTO;
import vn.ibex.digital_blog.domain.response.ResUpdateCommentDTO;
import vn.ibex.digital_blog.domain.response.ResultPaginationDTO;
import vn.ibex.digital_blog.service.ArticleService;
import vn.ibex.digital_blog.service.CommentService;
import vn.ibex.digital_blog.util.SecurityUtil;
import vn.ibex.digital_blog.util.annotation.ApiMessage;
import vn.ibex.digital_blog.util.error.IdInvalidException;

// Read Articles (pagination)
// Save Article
// Delete Article
//
// thiếu comment
// Save Comment to Article

@RestController
@RequestMapping("/api")
public class NewsController {
    private final ArticleService articleService;
    private final CommentService commentService;
    public NewsController(ArticleService articleService, CommentService commentService) {
        this.articleService = articleService;
        this.commentService = commentService;
    }

    @PostMapping("/articles")
    @ApiMessage("Create a new article")
    public ResponseEntity<ResCreateArticleDTO> create(@Valid @RequestBody Article article){
         String email = SecurityUtil.getCurrentUserLogin().isPresent()
        ? SecurityUtil.getCurrentUserLogin().get()
        : "";

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.articleService.create(article, email));
    }

    @PutMapping("/articles")
    @ApiMessage("Update an article")
    public ResponseEntity<ResUpdateArticleDTO> update(@Valid @RequestBody Article article)
    throws IdInvalidException{
        Optional<Article> currentArticle = this.articleService.fetchArticleById(article.getId());

        if(!currentArticle.isPresent()){
            throw new IdInvalidException("Article with id = "+ article.getId()+"is not found");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.articleService.update(article, currentArticle.get()));
    }

    @DeleteMapping("/articles/{id}")
    @ApiMessage("Delete an article")
    public ResponseEntity<Void> delete(@PathVariable long id)
    throws IdInvalidException{
        Optional<Article> curentArticle = this.articleService.fetchArticleById(id);
        if(!curentArticle.isPresent()){
            throw new IdInvalidException("Article with id = "+ id+"is not found");
            }
        this.articleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/articles/{id}")
    @ApiMessage("fetch an article by id")
    public ResponseEntity<Article> getArticle(@PathVariable("id") long id)
    throws IdInvalidException{
        Optional<Article> curentArticle = this.articleService.fetchArticleById(id);
        if(!curentArticle.isPresent()){
            throw new IdInvalidException("Article with id = "+ id+"is not found");
        }
        return ResponseEntity.ok(curentArticle.get());
    }

    @GetMapping("/articles")
    @ApiMessage("fetch all articles")
    // thiếu filter
    public ResponseEntity<ResultPaginationDTO> getAllArticle(
        //@Filter Specification<Article> spec,
        Pageable pageable){
            
            return ResponseEntity.status(HttpStatus.OK)
                    .body(this.articleService.fetchAllArticle(pageable));
        }


    @PostMapping("/articles/{id}/comments")
    @ApiMessage("Create a new comment")
    public ResponseEntity<ResCreateCommentDTO> createComment(@PathVariable("id") long articleId, @Valid @RequestBody String content)
    throws IdInvalidException{
        Optional<Article> article = this.articleService.fetchArticleById(articleId);
        if(!article.isPresent()){
            throw new IdInvalidException("Article with id = "+ articleId+"is not found");
        }

        String email = SecurityUtil.getCurrentUserLogin().isPresent()
        ? SecurityUtil.getCurrentUserLogin().get()
        : "";
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.commentService.saveComment(content, articleId, email));
    }

    @PostMapping("/articles/{id}/comments/{commentId}")
    @ApiMessage("Update a comment")
    public ResponseEntity<ResUpdateCommentDTO> updateComment(
    @PathVariable("id") long articleId, 
    @PathVariable("commentId") long commentId, 
    @Valid @RequestBody String content)
        throws IdInvalidException{
            // Optional<Article> article = this.articleService.fetchArticleById(articleId);
            // long userId=0;
            // if(!article.isPresent()){
            //     throw new IdInvalidException("Article with id = "+ articleId+"is not found");
            // }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(this.commentService.updateComment(commentId, content));
        }


}
