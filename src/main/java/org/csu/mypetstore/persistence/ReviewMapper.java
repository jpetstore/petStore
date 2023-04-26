package org.csu.mypetstore.persistence;

import org.csu.mypetstore.domain.Item;
import org.csu.mypetstore.domain.Product;
import org.csu.mypetstore.domain.Review;
import org.csu.mypetstore.domain.User;

import java.util.List;
import java.util.Map;

public interface ReviewMapper {

    boolean insertReview(Review review);//增加一条评论
    List<Review> getReviewListByItemId(String productId);
}
