package com.app.spotick.util.search;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AreaFilter {
    private String city;
    private List<String> address = new ArrayList<>();
}
