package com.starstar.repositories;

import com.starstar.models.Doc;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocRepository extends MongoRepository<Doc, String> {

}
