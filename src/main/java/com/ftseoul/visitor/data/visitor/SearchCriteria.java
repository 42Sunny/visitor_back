package com.ftseoul.visitor.data.visitor;

public enum SearchCriteria {
    VISITOR_NAME("Visitor", "name"),
    VISITOR_PHONE("Visitor", "phone"),
    VISITOR_STATUS("Visitor", "status"),
    VISITOR_ORGANIZATION("Visitor", "organization"),
    STAFF_DEPARTMENT("Staff", "department"),
    STAFF_NAME("Staff", "name"),
    STAFF_PHONE("Staff", "phone");

   private String entity;
   private String key;

    SearchCriteria(String entity, String key) {
        this.entity = entity;
        this.key = key;
    }

    public String getEntityName() {
        return entity;
    }

    public String getKey() {
        return key;
    }

    public boolean isName() {
        return getKey().equals("name");
    }

    public boolean isPhone() {
        return getKey().equals("phone");
    }
}
