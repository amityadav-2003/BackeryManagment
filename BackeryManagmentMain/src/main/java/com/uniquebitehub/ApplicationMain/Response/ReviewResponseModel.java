package com.uniquebitehub.ApplicationMain.Response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReviewResponseModel {

    private Long id;
    private Long userId;
    private Long productId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
