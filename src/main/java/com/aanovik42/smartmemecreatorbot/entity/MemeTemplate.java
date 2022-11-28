package com.aanovik42.smartmemecreatorbot.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemeTemplate {

    private Long id;
    private String name;
    private String templateImageUrl;
    private String sampleImageUrl;
    private int width;
    private int height;
    private int boxCount;
}
