package vn.ibex.digital_blog.service;

import java.util.Optional;

import javax.naming.spi.DirStateFactory.Result;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.ibex.digital_blog.domain.Article;
import vn.ibex.digital_blog.domain.response.ResCreateArticleDTO;
import vn.ibex.digital_blog.domain.response.ResUpdateArticleDTO;
import vn.ibex.digital_blog.domain.response.ResultPaginationDTO;
import vn.ibex.digital_blog.repository.ArticleRepository;
import vn.ibex.digital_blog.util.error.IdInvalidException;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public ResCreateArticleDTO create (Article article){
        Article currentArticle = this.articleRepository.save(article);

        // convert response
        ResCreateArticleDTO resCreateArticleDTO = new ResCreateArticleDTO();
        resCreateArticleDTO.setId(currentArticle.getId());
        resCreateArticleDTO.setTitle(currentArticle.getTitle());
        resCreateArticleDTO.setContent(currentArticle.getContent());
        resCreateArticleDTO.setCreatedBy(currentArticle.getCreatedBy());
        resCreateArticleDTO.setCreatedAt(currentArticle.getCreatedAt());

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

        articleInDB.setTitle(article.getTitle());
        articleInDB.setContent(article.getContent());
        articleInDB.setUpdatedAt(article.getUpdatedAt());
        
        Article currentArticle = this.articleRepository.save(articleInDB);

        // convert response
        ResUpdateArticleDTO resUpdateArticleDTO = new ResUpdateArticleDTO();
        resUpdateArticleDTO.setId(currentArticle.getId());
        resUpdateArticleDTO.setTitle(currentArticle.getTitle());
        resUpdateArticleDTO.setContent(currentArticle.getContent());
        resUpdateArticleDTO.setCreatedBy(currentArticle.getCreatedBy());
        resUpdateArticleDTO.setUpdatedAt(currentArticle.getUpdatedAt());

        return resUpdateArticleDTO;
    }

    public void delete(long id){
        this.articleRepository.deleteById(id);
    }

    public ResultPaginationDTO fetchAllArticle(Pageable pageable){
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta= new ResultPaginationDTO.Meta();

        Page<Article> articlePage = this.articleRepository.findAll(pageable);

        meta.setPage(pageable.getPageNumber()+1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(articlePage.getTotalPages());
        meta.setTotal(articlePage.getTotalElements());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(articlePage.getContent());
         return resultPaginationDTO;

    }

     

    
}