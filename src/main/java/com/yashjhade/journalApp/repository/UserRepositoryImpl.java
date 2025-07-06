package com.yashjhade.journalApp.repository;

import com.yashjhade.journalApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository// ← so Spring can pick it up
public class UserRepositoryImpl {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> getUserForSA() {

        Query query = new Query();
        query.addCriteria(
                Criteria.where("email")
                        .regex("^[A-Za-z0-9._%+-]+@gmail\\.com$", "i"));
        query.addCriteria(
                Criteria.where("sentimentAnalysis")
                        .is(true));

        return mongoTemplate.find(query, User.class);
    }
}

