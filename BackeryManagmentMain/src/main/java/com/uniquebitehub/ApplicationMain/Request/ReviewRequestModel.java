package com.uniquebitehub.ApplicationMain.Request;

import lombok.Data;

@Data
public class ReviewRequestModel {

    private Long id;          // for update
    private Long userId;
    private Long productId;
    private Integer rating;
    private String comment;
}
