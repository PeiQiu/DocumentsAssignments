package com.starstar.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.WriteResult;
import com.starstar.Utils.HttpUtils;
import com.starstar.models.DocNumber;
import com.starstar.models.DocResult;
import com.starstar.models.Review;
import com.starstar.models.User;
import com.starstar.services.ReviewService;
import com.starstar.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
@PreAuthorize("hasRole('ROLE_USER')")
public class UserApiController {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private ReviewService reviewService;
    @RequestMapping( value="docs/{username}", method= RequestMethod.GET )
    public List<Review> getReviews(@PathVariable("username") String username,@RequestParam(required = false) String title,
                                   @RequestParam(required = false) String zipcode, @RequestParam(required = false) String type,
                                   @RequestParam(required = false) Boolean significance,@RequestParam(required = false) Boolean isReviewed){

        List<Review> reviews=reviewService.loadReviews(username);
        Map<String,Review> map=new HashMap<>();
        List<Review> temp=new ArrayList<>();

        if(title!=null&&title.length()>0){
            for(Review review:reviews){
                if(review.getDoc().getTitle().toLowerCase().contains(title.toLowerCase())){
                    temp.add(review);
                }
            }
            reviews.clear();
            reviews.addAll(temp);
            temp.clear();
        }
        if(type!=null&&type.length()>0){
            for(Review review:reviews){
                if(type.length()>=4&&review.getDoc().getType().toLowerCase().contains(type.toLowerCase().substring(0,3))){
                    temp.add(review);
                }
            } reviews.clear();
            reviews.addAll(temp);
            temp.clear();
        }
        if(significance!=null){
            for(Review review:reviews){
                if(review.getDoc().getSignificant()==significance){
                    temp.add(review);
                }
            } reviews.clear();
            reviews.addAll(temp);
            temp.clear();
        }
        if(isReviewed!=null){
            for(Review review:reviews){
                if(isReviewed){
                    if(review.getRank()>0){
                        temp.add(review);
                    }
                }else {
                    if(review.getRank()==-1){
                        temp.add(review);
                    }
                }

            } reviews.clear();
            reviews.addAll(temp);
            temp.clear();
        }

        if(zipcode!=null&&zipcode.length()>0){
            ObjectMapper objectMapper = new ObjectMapper();
            ArrayList<DocNumber> docs=new ArrayList<>();
            DocResult docResult=new DocResult();
            String url="https://www.federalregister.gov/api/v1/articles.json?fields[]=document_number&per_page=1000&conditions[near][location]="+zipcode;
            docResult.setNext_page_url(url);
            String json=null;
            try {
                while (docResult.getNext_page_url()!=null){
                    json= HttpUtils.get(docResult.getNext_page_url());
                    docResult=objectMapper.readValue(json,DocResult.class);
                    docs.addAll(docResult.getResults());
                }
               for(DocNumber docNumber:docs){
                    for (Review review: reviews){
                        if(docNumber.getDocument_number().equals(review.getDoc().getDocument_number())){
                            temp.add(review);
                        }
                    }
               }
               reviews.clear();
                reviews.addAll(temp);
                temp.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for(Review review:reviews){
            map.put(review.getBelongtoUsername()+review.getDoc().getDocument_number(),review);
        }

        return new ArrayList<>(map.values());
    }
@RequestMapping(value = "{uid}/docs/{doc_num}/rank",method = RequestMethod.PUT)
    public int rate(@PathVariable("uid") String uid,@PathVariable("doc_num") String doc_num,@RequestBody Integer rank){
    User user=null;
    try {
        user=userService.loadUserByUserId(uid);
            Query query=new Query();
            query.addCriteria(Criteria.where("belongtoUsername").is(user.getUsername()));
            query.addCriteria(Criteria.where("doc.document_number").is(doc_num));
        WriteResult wr=mongoTemplate.updateMulti(query,Update.update("rank", rank),Review.class);
        return wr.getN();
        } catch (Exception e) {
        e.printStackTrace();
    }
    return -1;
}

}
