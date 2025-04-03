package com.franchises.develop.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "branches")
public class Branch {
    @Id
    private String id;
    private String name;
    private List<String> productsId = new ArrayList<>();
}
