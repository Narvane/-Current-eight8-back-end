package com.myportfolio.eightg.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserObject {

    private List<Object> attributes;

}
