package com.example.storereceivetest.itemsetting;

public class CourseModel {

    private String course_name;
    private String course_rating;
    private int course_image;

    public CourseModel(String course_name, String course_rating) {
        this.course_name = course_name;
        this.course_rating = course_rating;
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

}


