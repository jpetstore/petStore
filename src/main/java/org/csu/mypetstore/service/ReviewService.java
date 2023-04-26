package org.csu.mypetstore.service;

import org.csu.mypetstore.domain.Review;
import org.csu.mypetstore.persistence.ReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewMapper reviewMapper;
    public List<Review> getCategoryList(String Itemid)
    {
        return  reviewMapper.getReviewListByItemId(Itemid);
    }
    public boolean insertReview(Review review)
    {
        return   reviewMapper.insertReview(review);
    }
}
