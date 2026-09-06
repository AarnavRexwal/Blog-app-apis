package com.codewithaarnav.blog.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentDto {

    private Integer commentId;

    private String content;

    private Integer userId;

    private Integer postId;
}