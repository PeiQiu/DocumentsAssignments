package com.starstar.controllers;

import com.starstar.Utils.HttpUtils;
import com.starstar.Utils.ListUtils;
import com.starstar.models.Doc;
import com.starstar.models.DocList;
import com.starstar.models.Review;
import com.starstar.models.User;
import com.starstar.services.DocListService;
import com.starstar.services.ReviewService;
import com.starstar.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminApiController {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private DocListService docListService;
    @Autowired
    private ReviewService reviewService;

    @ResponseBody
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<User> getUsers(@RequestParam(value = "role", required = false) String role) {
        List<User> users = userService.findAll();
        List<User> results = new ArrayList<>();
        if (role != null) {
            for (User user : users) {
                if (user.hasRole(role)) {
                    results.add(user);
                }
            }

            return results;
        }

        return users;
    }
    @ResponseBody
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public User createUsers(@RequestBody  User user) {
        user.setIsAccountNonExpired(true);
          user.setCredentialsNonExpired(true);
          user.setIsEnabled(true);
          user.setIsAccountNonLocked(true);
        return userService.save(user);
    }
    @RequestMapping(value = "/users/role/{id}",method = RequestMethod.PUT)
    public  User updateRole(@PathVariable("id") String id,
                            @RequestBody String role) {
        try {
            User user=userService.loadUserByUserId(id);
            if(user.hasRole(role)){
                user.removeRole(role);
            }else {
                user.addRole(role);
            }
            return userService.save(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/users/enable/{id}",method = RequestMethod.PUT)
    public  User updateEnable(@PathVariable("id") String id) {
        try {
            User user=userService.loadUserByUserId(id);
            user.setIsEnabled(!user.isEnabled());
            return userService.save(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/docs", method = RequestMethod.GET)
    public String getDocs(@RequestParam String page, @RequestParam(required = false) String title,
                          @RequestParam(required = false) String zipcode, @RequestParam(required = false) String type,
                          @RequestParam(required = false) Integer significant) throws MalformedURLException {
        String url = "https://www.federalregister.gov/api/v1/articles.json?page=" + page;
        if (zipcode != null && zipcode.length() > 0) {
            url += "&conditions[near][location]=" + zipcode;
        }
        if (title != null && title.length() > 0) {
            url += "&conditions[term][title]=" + title;
        }
        if (type != null && type.length() > 0) {
            url += "&conditions[type][]=" + type;
        }
        if (significant != null) {
            url += "&conditions[significant]=" + significant;
        }
        url += "&fields[]=html_url&fields[]=significant&fields[]=title&fields[]=type&fields[]=abstract&fields[]=abstract&fields[]=document_number&order=relevance";
        return HttpUtils.get(url);
    }

    @ResponseBody
    @RequestMapping(value = "/doclist", method = RequestMethod.POST)
    public DocList createDocList(@RequestBody DocList docList) {
        List<Doc> docs = docList.getDocs();
        List<String> users = docList.getUsers();
        List<Review> reviews = new ArrayList<>();
        DocList list = docListService.save(docList);
        for (int i = 0; i < docs.size(); i++) {
            for (int j = 0; j < users.size(); j++) {
                reviews.add(new Review(list.getId(), users.get(j), docs.get(i), -1));
            }
        }
        reviewService.saveAll(reviews);
        return list;
    }

    @ResponseBody
    @RequestMapping(value = "/doclist", method = RequestMethod.GET)
    public List<DocList> getDocList() {
        return docListService.findAll();
    }

    @ResponseBody
    @RequestMapping(value = "/doclist/{docId}", method = RequestMethod.PUT)
    public DocList updateDocList(@RequestBody DocList docList, @PathVariable("docId") String docId) throws Exception {
        List<Doc> docs = docList.getDocs();
        List<String> users = docList.getUsers();
        List<Review> reviews = new ArrayList<>();
        DocList list = docListService.loadById(docId);
        List<Doc> deleteDocs = ListUtils.getDocsNotIn(docs, list.getDocs());
        List<Doc> addDocs = ListUtils.getDocsNotIn(list.getDocs(), docs);
        List<String> deleteUsers = ListUtils.getUserNameNotIn(users, list.getUsers());
        List<String> addUsers = ListUtils.getUserNameNotIn(list.getUsers(), users);
        List<Doc> remainDocs = new ArrayList<>();
        remainDocs.addAll(list.getDocs());
        remainDocs.removeAll(deleteDocs);
        List<String> remainUsers = new ArrayList<>();
        remainUsers.addAll(list.getUsers());
        remainUsers.removeAll(deleteUsers);
        //delete Review have deleteUser
        for (String du : deleteUsers) {
            Query query = new Query();
            query.addCriteria(Criteria.where("belongtoUsername").is(du));
            query.addCriteria(Criteria.where("docListId").is(list.getId()));
            mongoTemplate.findAllAndRemove(query, Review.class);
        }
        //delete Review have deleteDoc
        for (Doc doc : deleteDocs) {
            Query query = new Query();
            query.addCriteria(Criteria.where("doc.document_number").is(doc.getDocument_number()));
            query.addCriteria(Criteria.where("docListId").is(list.getId()));
            mongoTemplate.findAllAndRemove(query, Review.class);
        }
        //add Review
        for (Doc doc : remainDocs) {
            for (String un : addUsers) {
                reviews.add(new Review(list.getId(), un, doc, -1));
            }
        }
        for (Doc doc : addDocs) {
            for (String un : addUsers) {
                reviews.add(new Review(list.getId(), un, doc, -1));
            }
            for (String un : remainUsers) {
                reviews.add(new Review(list.getId(), un, doc, -1));
            }
        }
        reviewService.saveAll(reviews);
        list.setDocs(docs);
        list.setUsers(users);
        return docListService.save(list);
    }

    @ResponseBody
    @RequestMapping(value = "/users/profile/{id}",method = RequestMethod.PUT)
    public  User updateUserProfile(@PathVariable("id") String id,
                               @RequestBody User u){

        try {
            User user=userService.loadUserByUserId(id);
            if(user!=null){
                if(u.getPassword()!=null){
                    user.setPassword(u.getPassword());
                }

                user = userService.save(user);
                user.setPassword("");
                return user;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new User();
    }


}
