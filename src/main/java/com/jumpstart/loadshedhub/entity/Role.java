package com.jumpstart.loadshedhub.entity;

//defines strict access levels
//using enum prevents spelling mistakes in the database
public enum Role {
    //can only view locations & submit check-in reports
    ROLE_CITIZEN,

    //registers business & amenities offered
    ROLE_BUSINESS_OWNER,

    //verifies system admin who can add or delete hubs
    ROLE_ADMIN
}