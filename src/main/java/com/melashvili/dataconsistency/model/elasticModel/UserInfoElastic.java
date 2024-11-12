package com.melashvili.dataconsistency.model.elasticModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Document(indexName = "userinfoelastic")
public class UserInfoElastic {

    private int id;

    private Date birthDate;

    private String address;
}
