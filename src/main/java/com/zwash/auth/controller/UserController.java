package com.zwash.auth.controller;

import java.util.ServiceLoader;

import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.zwash.auth.exceptions.IncorrectTokenException;
import com.zwash.auth.exceptions.UserIsNotActiveException;
import com.zwash.auth.pojos.LoggedUser;
import com.zwash.auth.pojos.SignInfo;
import com.zwash.auth.security.JwtUtils;
import com.zwash.auth.service.UserService;
import com.zwash.common.pojos.User;

import io.jsonwebtoken.Claims;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("v1/users")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private JwtUtils jwtUtils;

	Logger logger = LoggerFactory.getLogger(UserController.class);

	@GetMapping("/users")
	public ModelAndView home() {
		return new ModelAndView("users");
	}

	@GetMapping("/welcome")
	public String message() {
		return "Hello there i am athentiction service";
	}
	@PostMapping(value = "/signin", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Signs in an existing user.")
	@Parameters({
			@Parameter(name = "username", description = "The username of the user to sign in.", required = true),
			@Parameter(name = "password", description = "The password of the user to sign in.", required = true) })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "User signed in successfully."),
			@ApiResponse(responseCode = "401", description = "Invalid username or password."),
			@ApiResponse(responseCode = "500", description = "Internal server error.") })
	public ResponseEntity<LoggedUser> signIn(@RequestBody SignInfo userInfo) throws Exception {

		LoggedUser signedUser;
		try {

			signedUser = userService.signIn(userInfo.getUsername(), userInfo.getPassword());
			userService.setDeviceRegistrationToken(signedUser.getId(), userInfo.getDeviceRegistrationToken());
			if (signedUser instanceof LoggedUser) {
				if (signedUser.isActive()) {

					logger.info("User has signed in successfully " + userInfo.getUsername());
					return new ResponseEntity<>(signedUser, HttpStatus.OK);
				} else {

					logger.error("User is not actived" + userInfo.getUsername());
					throw new UserIsNotActiveException(userInfo.getUsername());
				}

			} else {
				logger.info("User has not signed in Inncorrect password or username " + userInfo.getUsername());
				return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
			}

		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * Registers a new user.
	 *
	 * @param registerDetails the user registration details in JSON format
	 * @return a response entity indicating whether the registration was successful
	 *         or not
	 * @throws Exception if there is an error during the registration process
	 */
	@PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Registers a new user.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "User created successfully."),
			@ApiResponse(responseCode = "406", description = "User already exists."),
			@ApiResponse(responseCode = "500", description = "Internal server error.") })
	public ResponseEntity<String> register(@RequestBody User user) throws Exception {
		User userCreated;
		try {
			userCreated = userService.register(user, false);

			if (userCreated instanceof User) {
				logger.info("User has registered  successfully " + user.getUsername());
				return new ResponseEntity<>(userCreated.getString(), HttpStatus.CREATED);
			} else {
				logger.error("User has not registered  " + user.getUsername());
				return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
			}
		} catch (DataIntegrityViolationException dx) {
			logger.error("User already exists  " + user.getUsername());
			return new ResponseEntity<>(

					"User already exists", HttpStatus.CONFLICT);
		}

		catch (Exception e) {
			throw e;
		}

	}

	@PostMapping(value = "/register/admin", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Registers a new admin.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Admin created successfully."),
			@ApiResponse(responseCode = "406", description = "Admin already exists."),
			@ApiResponse(responseCode = "500", description = "Internal server error.") })
	public ResponseEntity<String> registerAdmin(@RequestBody User user) throws Exception {
		User adminCreated;
		try {
			adminCreated = userService.register(user, true);

			if (adminCreated instanceof User) {
				logger.info("Admin has registered successfully: " + user.getUsername());
				return new ResponseEntity<>("Admin created", HttpStatus.OK);
			} else {
				logger.error("Admin has not registered: " + user.getUsername());
				return new ResponseEntity<>("Admin not created", HttpStatus.NOT_ACCEPTABLE);
			}
		} catch (DataIntegrityViolationException dx) {
			logger.error("Admin already exists: " + user.getUsername());
			return new ResponseEntity<>("Admin already exists", HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			throw e;
		}
	}

	@PostMapping(value = "/changepassword", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Change password for a user.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Password changed successfully."),
			@ApiResponse(responseCode = "401", description = "Unauthorized access."),
			@ApiResponse(responseCode = "500", description = "Internal server error.") })
	public ResponseEntity<String> changePassword(@RequestBody User user) throws Exception {


		try {
			Claims cl = jwtUtils.verifyJWT(user.getToken());
			user.setUsername(cl.getId());
		} catch (Exception ex) {
			logger.error("The token is not valid! " + user.getUsername());
			throw new IncorrectTokenException("The token is not valid!");
		}

		boolean changed;
		try {
			changed = userService.changePassword(user.getUsername(), user.getPassword());

			if (changed) {
				logger.info("The password changed! " + user.getUsername());
				return new ResponseEntity<>("Password changed", HttpStatus.OK);
			} else {
				logger.error("The password is not changed! " + user.getUsername());
				return new ResponseEntity<>("Password not changed", HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (Exception e) {
			logger.error("The password is not changed! " + e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * Validates if the user is signed in.
	 *
	 * @param token JWT token of the user
	 * @return Response indicating whether the user is signed in or not
	 * @throws Exception if there is an error while validating the user
	 */
	@GetMapping(value = "/validate", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Validates if the user is signed in.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "User is signed in."),
			@ApiResponse(responseCode = "200", description = "User is not signed in."),
			@ApiResponse(responseCode = "500", description = "Internal server error.") })
	public Response validateIfSigned(
			@Parameter(description = "JWT token of the user", required = true) @QueryParam("token") String token)
			throws Exception {

		boolean valid = false;
		try {
			valid = userService.validateSignIn(token);
		} catch (Exception exp) {

			logger.error("validation error! " + exp.getMessage());
			return Response.status(500).entity(exp.getMessage()).build();
		}
		if (valid) {
			logger.info("The token is valid! " + token);
			return Response.ok(valid).build();
		} else {
			logger.error("The token is not valid! " + token);
			return Response.ok(false).build();
		}
	}

	@PostMapping(value = "/getsecrets", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Get secret question and answer for a user.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Secret question and answer retrieved successfully."),
			@ApiResponse(responseCode = "500", description = "Internal server error.") })
	public Response getSecretQuestionAnswer(@RequestBody User user) throws Exception {
		try {
			user.setSecretAnswer(userService.getSecretQuestionAnswer(user.getUsername()));

			if (user instanceof User) {

				logger.info("The Secrets are returned! of" + user.getUsername());
				return Response.ok(true).build();
			}
		} catch (Exception e) {
			logger.error("The Secrets are not returned! of" + user.getUsername() + " because : " + e.getMessage());
			return Response.status(500).entity(e.getMessage()).build();
		}
		logger.error("The Secrets are not returned! of" + user.getUsername() + " because : User is not found!");
		return Response.status(500).entity("User not found").build();
	}

	public static UserService getUserService() throws Exception {
		try {
			ServiceLoader<UserService> serviceLoader = ServiceLoader.load(UserService.class);
			UserService userService = serviceLoader.iterator().next();

			return userService;
		} catch (Exception e) {

			throw e;
		}

	}
}