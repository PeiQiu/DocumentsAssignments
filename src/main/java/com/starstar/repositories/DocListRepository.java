package com.starstar.repositories;

import com.starstar.models.DocList;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocListRepository extends MongoRepository<DocList, String> {

}
