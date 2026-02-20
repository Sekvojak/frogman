module lab01 {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires org.apache.logging.log4j;

    requires static lombok;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    requires static jakarta.annotation;
    requires spring.data.jpa;
    requires spring.web;
    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires org.json;

    opens lab;

    exports lab;

}