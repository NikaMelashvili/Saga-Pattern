package com.melashvili.dataconsistency.repositories;

import com.melashvili.dataconsistency.model.elasticModel.UserElastic;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserElasticRepository extends ElasticsearchRepository<UserElastic, Integer> {
}
