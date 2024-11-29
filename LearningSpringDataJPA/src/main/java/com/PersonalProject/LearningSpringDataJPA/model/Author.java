package com.PersonalProject.LearningSpringDataJPA.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.sql.Date;
@Entity
@Table(name = "author_tbl")
public class Author {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "first_name",length = 25,nullable = false)
    private String firstName;
    private String LastName;
    @Column(name = "email",unique = true)
    private String email;
    @Column(name = "dbCreated_at",updatable = false)
    private Date createdAt;
}
