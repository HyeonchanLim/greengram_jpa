package com.green.greengram.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class FeedPicIds implements Serializable {

    private Long feedId;

    private String pic;
}
