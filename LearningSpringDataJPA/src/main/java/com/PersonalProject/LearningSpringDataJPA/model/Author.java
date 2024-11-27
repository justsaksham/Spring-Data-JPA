package com.PersonalProject.LearningSpringDataJPA.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.sql.Date;
@Entity
public class Author {
    @Id
    @GeneratedValue
    private Integer id;
    private String firstName;
    private String LastName;
    private String email;
    private Date createdAt;
}
