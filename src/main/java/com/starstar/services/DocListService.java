package com.starstar.services;

import com.starstar.models.DocList;
import com.starstar.repositories.DocListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocListService {
    @Autowired
    private DocListRepository docListRespository;
    public DocList save(DocList docList ) {
        return docListRespository.save(docList);
    }
    public List<String> findReviewDoc;
    public List<DocList> findAll() {
        return docListRespository.findAll();
    }
    public DocList loadById(String id) throws Exception {
        DocList docList = docListRespository.findOne(id);
        if(docList == null) throw new Exception("Id not exists");
        return docList;
    }
}
