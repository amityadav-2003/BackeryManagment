package com.uniquebitehub.ApplicationMain.Converter.Entity;

import org.springframework.stereotype.Component;

import com.uniquebitehub.ApplicationMain.Entity.Review;
import com.uniquebitehub.ApplicationMain.Response.ReviewResponseModel;

@Component
public class ReviewEntityToModelConverter {

    public ReviewResponseModel convert(Review review) {

        ReviewResponseModel model = new ReviewResponseModel();
        model.setId(review.getId());
        model.setUserId(review.getUser().getId());
        model.setProductId(review.getProduct().getId());
        model.setRating(review.getRating());
        model.setComment(review.getComment());
        model.setCreatedAt(review.getCreatedAt());

        return model;
    }
}
