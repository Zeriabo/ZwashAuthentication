package com.zwash.auth.controller;

import java.util.ServiceLoader;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

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
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/")
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
	@ApiOperation(value = "Signs in an existing user.", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "username", value = "The username of the user to sign in.", required = true, dataType = "string", paramType = "body"),
			@ApiImplicitParam(name = "password", value = "The password of the user to sign in.", required = true, dataType = "string", paramType = "body") })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "User signed in successfully."),
			@ApiResponse(code = 401, message = "Invalid username or password."),
			@ApiResponse(code = 500, message = "Internal server error.") })
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
	@ApiOperation(value = "Registers a new user.", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "User created successfully."),
			@ApiResponse(code = 406, message = "User already exists."),
			@ApiResponse(code = 500, message = "Internal server error.") })
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
	@ApiOperation(value = "Registers a new admin.", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Admin created successfully."),
			@ApiResponse(code = 406, message = "Admin already exists."),
			@ApiResponse(code = 500, message = "Internal server error.") })
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
	@ApiOperation(value = "Change password for a user.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Password changed successfully."),
			@ApiResponse(code = 401, message = "Unauthorized access."),
			@ApiResponse(code = 500, message = "Internal server error.") })
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
	@ApiOperation(value = "Validates if the user is signed in.", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "User is signed in."),
			@ApiResponse(code = 200, message = "User is not signed in."),
			@ApiResponse(code = 500, message = "Internal server error.") })
	public Response validateIfSigned(
			@ApiParam(value = "JWT token of the user", required = true) @QueryParam("token") String token)
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
	@ApiOperation(value = "Get secret question and answer for a user.", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Secret question and answer retrieved successfully."),
			@ApiResponse(code = 500, message = "Internal server error.") })
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