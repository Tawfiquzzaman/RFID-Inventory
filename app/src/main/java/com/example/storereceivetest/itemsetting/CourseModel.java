package com.example.storereceivetest.itemsetting;

public class CourseModel {

    private String course_name;
    private String course_rating;
    private int course_image;

    // Constructor
    public CourseModel(String course_name, String course_rating) {
        this.course_name = course_name;
        this.course_rating = course_rating;
        //this.course_image = course_image;
    }

    // Getter and Setter
    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCourse_rating() {
        return course_rating;
    }

    public void setCourse_rating(String course_rating) {
        this.course_rating = course_rating;
    }

//    public int getCourse_image() {
//        return course_image;
//    }
//
//    public void setCourse_image(int course_image) {
//        this.course_image = course_image;
//    }
}


