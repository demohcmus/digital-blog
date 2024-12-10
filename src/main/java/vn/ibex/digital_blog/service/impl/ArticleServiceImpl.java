package vn.ibex.digital_blog.service.impl;

import java.util.Optional;

import javax.naming.spi.DirStateFactory.Result;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.ibex.digital_blog.domain.Article;
import vn.ibex.digital_blog.domain.User;
import vn.ibex.digital_blog.domain.response.ResCreateArticleDTO;
import vn.ibex.digital_blog.domain.response.ResUpdateArticleDTO;
import vn.ibex.digital_blog.domain.response.ResultPaginationDTO;
import vn.ibex.digital_blog.repository.ArticleRepository;
import vn.ibex.digital_blog.repository.UserRepository;
import vn.ibex.digital_blog.service.ArticleService;
import vn.ibex.digital_blog.util.error.IdInvalidException;

@Service
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository,
    UserRepository userRepository){ 
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    public ResCreateArticleDTO create (Article article, String email){
        User user = this.userRepository.findByEmail(email);
        article.setUser(user);
        Article currentArticle = this.articleRepository.save(article);

        // convert response
        ResCreateArticleDTO resCreateArticleDTO = new ResCreateArticleDTO(
            currentArticle.getId(),
            currentArticle.getTitle(),
            currentArticle.getContent(),
            currentArticle.getCreatedBy(),
            currentArticle.getCreatedAt()
        );

        return resCreateArticleDTO;
    }

    public Optional<Article> fetchArticleById(long id){
        Optional<Article> article = this.articleRepository.findById(id);
        if(article.isPresent()){
            return article;
        }
        return null;
    }

    public ResUpdateArticleDTO update(Article article, Article articleInDB){

        if(article.getTitle()!=null)
        articleInDB.setTitle(article.getTitle());
        if(article.getContent()!=null)
        articleInDB.setContent(article.getContent());
        
        Article currentArticle = this.articleRepository.save(articleInDB);

        // convert response
        ResUpdateArticleDTO resUpdateArticleDTO = new ResUpdateArticleDTO(
            currentArticle.getId(),
            currentArticle.getTitle(),
            currentArticle.getContent(),
            currentArticle.getUpdatedAt()
        );

        return resUpdateArticleDTO;
    }

    public void delete(long id){
        this.articleRepository.deleteById(id);
    }

    public ResultPaginationDTO fetchAllArticle(Pageable pageable){
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();

        Page<Article> articlePage = this.articleRepository.findAll(pageable);

        ResultPaginationDTO.Meta meta= new ResultPaginationDTO.Meta(
            pageable.getPageNumber()+1,
            pageable.getPageSize(),
            articlePage.getTotalPages(),
            articlePage.getTotalElements()
        );

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(articlePage.getContent());
         return resultPaginationDTO;

    }

     

    
}
