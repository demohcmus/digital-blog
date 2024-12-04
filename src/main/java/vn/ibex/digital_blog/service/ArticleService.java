package vn.ibex.digital_blog.service;

import java.util.Optional;

import org.springframework.data.domain.Pageable;

import vn.ibex.digital_blog.domain.Article;
import vn.ibex.digital_blog.domain.response.ResCreateArticleDTO;
import vn.ibex.digital_blog.domain.response.ResUpdateArticleDTO;
import vn.ibex.digital_blog.domain.response.ResultPaginationDTO;

public interface ArticleService {
     ResCreateArticleDTO create(Article article, String email);

    Optional<Article> fetchArticleById(long id);

    ResUpdateArticleDTO update(Article article, Article articleInDB);

    void delete(long id);

    ResultPaginationDTO fetchAllArticle(Pageable pageable);
}
