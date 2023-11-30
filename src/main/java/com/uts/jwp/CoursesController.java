package com.uts.jwp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Binding;

import com.uts.jwp.domain.Courses;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class CoursesController {

    public static Map<String, Courses> courseMap = new HashMap<>();

    @GetMapping("/courses")
    public String getCourses(Model model) {
        model.addAttribute("courses", fetchCourses());
        return "index";
    }

    @GetMapping("/signup")
    public String showSignUpForm(Courses courses) {
        return "add-courses";
    }

    @PostMapping("/courses")
    public String addCourse(@Valid Courses course, BindingResult bindingResult, Model model) {

        validateCourseCode(course.getCourseCode(), bindingResult);
        validateTotSKS(course.getTotSKS(), bindingResult);
        validateFaculty(course.getFaculty(), bindingResult);

        if (bindingResult.hasErrors()) {
            return "add-courses";
        }

        if (isCourseCodeAlreadyExists(course.getCourseCode())) {
            throw new IllegalArgumentException("Course with courseCode:" + course.getCourseCode() + " already exists");
        }

        courseMap.put(course.getCourseCode(), course);
        model.addAttribute("courses", fetchCourses());
        return "index";
    }

    private void validateCourseCode(String courseCode, BindingResult bindingResult) {
        if (courseCode == null || !courseCode.matches("^PG\\d{3}$")) {
            ObjectError error = new ObjectError("courseCode", "Code Course must start with PG and end with 3 digits");
            bindingResult.addError(error);
        }
    }

    private void validateTotSKS(Integer totSKS, BindingResult bindingResult) {
        if (totSKS == null || totSKS < 1 || totSKS > 3) {
            ObjectError error = new ObjectError("totSKS", "Total SKS must be between 1 and 3");
            bindingResult.addError(error);
        }
    }

    private void validateFaculty(String faculty, BindingResult bindingResult) {
        if (faculty == null || !(faculty.equals("FE") || faculty.equals("FTI") || faculty.equals("FEB") ||
                faculty.equals("FT") || faculty.equals("FISSIP") || faculty.equals("FKDK"))) {
            ObjectError error = new ObjectError("faculty", "Invalid faculty. Choose from: FE, FTI, FEB, FT, FISSIP, FKDK");
            bindingResult.addError(error);
        }
    }

    private boolean isCourseCodeAlreadyExists(String courseCode) {
        return courseMap.containsKey(courseCode);
    }

	@GetMapping(value = "/courses/{courseCode}")
    public ResponseEntity<Courses> findCourses(@PathVariable("courseCode") String courseCode) {
        final Courses courses = courseMap.get(courseCode);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

	private static List<Courses> fetchCourses() {
        return courseMap.values().stream().toList();
    }


    @PostMapping(value = "/courses/{courseCode}")
    public String updateCourse(@PathVariable("courseCode") String courseCode,
    @Valid Courses course,
    BindingResult result, Model model) {

        validateTotSKS(course.getTotSKS(), result);
        validateFaculty(course.getFaculty(), result);

        final Courses courseToBeUpdated = courseMap.get(courseCode);
        if (courseToBeUpdated == null) {
        throw new IllegalArgumentException("Course with courseCode:" + courseCode + " not found");
        }

        if (!courseCode.equals(course.getCourseCode()) && isCourseCodeAlreadyExists(course.getCourseCode())) {
        throw new IllegalArgumentException("Course with courseCode:" + course.getCourseCode() + " already exists");
        }

        // Update other fields as needed
        courseMap.put(course.getCourseCode(), course);

        model.addAttribute("courses", fetchCourses());
        return "redirect:/courses";
    }


	@GetMapping("/edit/{courseCode}")
	public String showUpdateForm(@PathVariable("courseCode") String courseCode, Model model) {
		final Courses coursesToBeUpdate = courseMap.get(courseCode);
		if (coursesToBeUpdate == null) {
			throw new IllegalArgumentException("Course with code : " + courseCode + "is not found");

		}
		model.addAttribute("courses", coursesToBeUpdate);
		return "update-courses";
	}

	@GetMapping(value = "/courses/{courseCode}/delete")
    public String deleteStudent(@PathVariable("courseCode") String courseCode) {
        courseMap.remove(courseCode);
        return "redirect:/courses";
    }
}
