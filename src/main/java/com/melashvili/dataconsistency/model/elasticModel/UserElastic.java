package com.melashvili.dataconsistency.model.elasticModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(indexName = "userelastic")
public class UserElastic {

    private int id;

    private String firstName;

    private String lastName;

    private UserInfoElastic userInfo;
}
