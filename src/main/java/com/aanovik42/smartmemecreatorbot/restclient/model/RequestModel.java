package com.aanovik42.smartmemecreatorbot.restclient.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestModel {

    private Long templateId;
    private List<String> text;

}
