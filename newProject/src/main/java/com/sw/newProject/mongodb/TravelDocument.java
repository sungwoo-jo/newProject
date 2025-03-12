package com.sw.newProject.mongodb;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "travel")  // MongoDB에서 "travel" 컬렉션과 연결
public class TravelDocument {
    @Id
    private String id;
    private String destination;
    private String description;
    private Double rating;
}
