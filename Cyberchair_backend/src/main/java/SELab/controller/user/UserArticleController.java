package SELab.controller.user;

import SELab.controller.util.UtilController;
import SELab.domain.Author;
import SELab.request.user.ArticleDetailRequest;
import SELab.request.user.ArticleRequest;
import SELab.service.Service;
import SELab.service.user.UserArticleService;
import com.alibaba.fastjson.JSONArray;
import javafx.util.Pair;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class UserArticleController {

    private Service service;
    Logger logger = LoggerFactory.getLogger(UserArticleController.class);

    @Autowired
    public UserArticleController(Service service){
        this.service = service;
    }

    //get the detailed information about a article
    @GetMapping("/user/articleDetail")
    public ResponseEntity<?> getArticleDetail(String articleId){
        logger.debug("article detail get request received, article id = " + articleId);
        return ResponseEntity.ok(service.getArticleDetail(articleId));
    }

    //user submit a new article for a meeting
    @PostMapping("/user/articleSubmission")
    public ResponseEntity<?> submitNewArticle(
            @RequestParam("meetingName") String meetingName,
            @RequestParam("username") String username,
            @RequestParam("essayTitle") String essayTitle,
            @RequestParam("essayAbstract") String essayAbstract,
            @RequestParam("submitTime") String submitTime,
            @RequestParam("topic") Set<String> topic,
            @RequestParam("authors") String authors,
            @RequestParam("essayPDF") MultipartFile pdfFile,
            HttpServletRequest servletRequest

    ) {

        Set<Pair<Author, Integer>> authorArgument = generateAuthor(authors);

        ArticleRequest request = new ArticleRequest(
                meetingName, username, essayTitle, essayAbstract,
                submitTime, pdfFile, topic, authorArgument
        );
        String parentDir = servletRequest.getServletContext().getRealPath("src/resources/");
        return ResponseEntity.ok(service.submitNewArticle(request, parentDir));
    }

    //user update an existing paper
    @PostMapping("/user/updateArticle")
    public ResponseEntity<?> updateArticle(
            @RequestParam("articleId") String articleId,
            @RequestParam("meetingName") String meetingName,
            @RequestParam("username") String username,
            @RequestParam("essayTitle") String essayTitle,
            @RequestParam("essayAbstract") String essayAbstract,
            @RequestParam("submitTime") String submitTime,
            @RequestParam("topic") Set<String> topic,
            @RequestParam("authors") String authors,
            @RequestParam(value = "essayPDF", required = false) MultipartFile pdfFile,
            HttpServletRequest servletRequest

    ) {
        Set<Pair<Author, Integer>> authorArgument = generateAuthor(authors);

        ArticleRequest request = new ArticleRequest(
                meetingName, username, essayTitle, essayAbstract,
                submitTime, pdfFile, topic, authorArgument
        );


        String parentDir = servletRequest.getServletContext().getRealPath("src/resources/static/");
        return ResponseEntity.ok(service.updateArticle(articleId, request, parentDir));
    }


    @GetMapping("/user/reviews")
    public ResponseEntity<?> getAllReviewsOfArticle(String articleId){
        return ResponseEntity.ok(service.getReviewsOfArticle(articleId));
    }

    private Set<Pair<Author, Integer>> generateAuthor(String authors){
        List<Author> authorsList = JSONArray.parseArray(authors, Author.class);
        Set<Pair<Author, Integer>> authorArgument = new HashSet<>();
        int rank = 1;
        for (Author author: authorsList){
            authorArgument.add(new Pair<>(author, rank++));
        }
        return authorArgument;
    }





    //post a new article
//    @PostMapping("/user/articleSubmission")

}
