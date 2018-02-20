package com.starstar.services;

import com.starstar.models.Review;
import com.starstar.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRespository;
    public Review save(Review review) {
        return reviewRespository.save(review);
    }
    public void saveAll(List<Review> reviews){
        for(Review review:reviews){
            save(review);
        }
    }
    public Review loadById(String id) throws Exception {
       Review review = reviewRespository.findOne(id);
        if(review == null) throw new Exception("Id not exists");
        return review;
    }
    public List<Review> loadReviews(String username){
        return reviewRespository.findByBelongtoUsername(username);
    }
}
