package com.uts.jwp.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Courses {
    @NotBlank(message = "course name is required")
    @Pattern(regexp = "^PG.*", message = ("Course code must begin with 'PG'"))
    private String courseCode;

    @NotBlank(message = "course code is required")
    @Size(min = 5, max = 10)
    private String courseName;

    @NotNull(message = "TotSKS is required")
    @Min(value = 1, message = "Minimal 1 SKS")
    @Max(value = 3, message = "Maximal 3 SKS")
    private Integer totSKS;

    @NotBlank(message = "faculty is required")
    @Pattern(regexp = "^(FE|FTI|FT)$", message = ("Faculty must be one of (FE, FTI, FT)"))
    private String faculty;

    public Courses() {

    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }


    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public Integer getTotSKS() {
        return totSKS;
    }

    public void setTotSKS(Integer totSKS) {
        this.totSKS = totSKS;
    }

    


    




    

}
