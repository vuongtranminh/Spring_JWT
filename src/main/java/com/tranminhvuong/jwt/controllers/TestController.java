package com.tranminhvuong.jwt.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/test")
public class TestController {

	/**
	 * [GET] /api/v1/test/all
	 * @return String
	 */
	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}
	
	/**
	 * [GET] /api/v1/test/user
	 * @return String
	 */
	@GetMapping("/user")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public String userAccess() {
		return "User Content.";
	}

	/**
	 * [GET] /api/v1/test/moderator
	 * @return String
	 */
	@GetMapping("/moderator")
	@PreAuthorize("hasRole('MODERATOR')")
	public String moderatorAccess() {
		return "Moderator Board.";
	}

	/**
	 * [GET] /api/v1/test/admin
	 * @return String
	 */
	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return "Admin Board.";
	}
	
}
