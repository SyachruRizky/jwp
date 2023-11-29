package com.uts.jwp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.uts.jwp.domain.Courses;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoursesController {
    
    public static Map<String, Courses> coursesMap = new HashMap<>();

    @GetMapping("/courses")
    public List<Courses> getCourses() {
        return coursesMap.values().stream().toList();
    }

    @PostMapping("/courses")
    public ResponseEntity<String> addCourses(@Valid @RequestBody Courses courses) {
        coursesMap.put(courses.getCourseCode(), courses);
        Courses savedCourses = coursesMap.get(courses.getCourseCode());
        return new ResponseEntity<>("Courses with Code: " + savedCourses.getCourseCode() +
                "has been created", HttpStatus.OK);   
    }

    @GetMapping(value = "/courses/{code}")
	public ResponseEntity<Courses> findStudent(@PathVariable("code") String code){
		final Courses courses = coursesMap.get(code);
		return new ResponseEntity<>(courses, HttpStatus.OK);
	}

    @PutMapping(value = "/courses/{code}")
	public ResponseEntity<String> updateCourses(@PathVariable("code") String code, @RequestBody Courses courses){
		final Courses coursesToBeUpdate = coursesMap.get(courses.getCourseCode());
		coursesToBeUpdate.setCourseName(courses.getCourseName());
		coursesToBeUpdate.setTotSKS(courses.getTotSKS());
		coursesToBeUpdate.setFaculty(courses.getFaculty()); 

		coursesMap.put(courses.getCourseCode(), coursesToBeUpdate);
		return new ResponseEntity<String>("Courses with Code: " + coursesToBeUpdate.getCourseCode() + " has been updated", HttpStatus.OK);
	}

    @DeleteMapping(value = "/courses/{code}")
	public ResponseEntity<Void> deleteStudent(@PathVariable("code") String code){
		coursesMap.remove(code);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

    
        
    

}
