
module marsrover {

    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.swing;
    requires org.fxyz3d.core;

    opens marsrover;
    opens marsrover.model;
    opens marsrover.menu;
    opens marsrover.terrain;
    opens marsrover.entity.entities;
    opens marsrover.menu.menus;
    opens marsrover.entity;
    opens marsrover.model.models;

}