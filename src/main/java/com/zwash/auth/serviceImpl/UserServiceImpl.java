package com.zwash.auth.serviceImpl;

import java.util.Optional;
import java.util.ServiceLoader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.zwash.auth.exceptions.IncorrectCredentialsException;
import com.zwash.auth.exceptions.UserAlreadyExistsException;
import com.zwash.auth.exceptions.UserIsNotFoundException;
import com.zwash.auth.pojos.LoggedUser;
import com.zwash.auth.repository.UserRepository;
import com.zwash.auth.security.JwtUtils;
import com.zwash.auth.service.CarService;
import com.zwash.auth.service.TokenService;
import com.zwash.auth.service.UserService;

import io.jsonwebtoken.Claims;
import com.zwash.auth.pojos.User;
public class UserServiceImpl implements UserService {

	private static final long serialVersionUID = 1L;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	TokenService tokenService;
	
	@Autowired
	CarService carService;

	@Autowired
	JwtUtils jwtUtils;

	@Override
	public LoggedUser signIn(String username, String password) throws Exception {

		User user = userRepository.findByUsernameAndPassword(username, password);
		if (user == null) {
			throw new IncorrectCredentialsException("Incorrect input!");
		}
		LoggedUser loggedUser = new LoggedUser();
		loggedUser.setId(user.getId());
		loggedUser.setUsername(user.getUsername());
		loggedUser.setActive(user.isActive());
		loggedUser.setDateOfBirth(user.getDateOfBirth());
		loggedUser.setFirstName(user.getFirstName());
		loggedUser.setLastName(user.getLastName());
		loggedUser.setAdmin(user.isAdmin());
		// Create a JWTToken
		Long id = loggedUser.getId();
		String jwt = tokenService.createJWT(id.toString(), "Java", loggedUser.getUsername(), 1232134356);
		loggedUser.setToken(jwt);

		return loggedUser;
	}

	@Override
	public User register(User user, boolean isAdmin) throws Exception {

		Optional<User> found = userRepository.findByUsername(user.getUsername());
		 if (found.isPresent()) {
		        // User is found
		        throw new UserAlreadyExistsException(user.getUsername());
		    }

		user.setActive(true);
		user.setAdmin(false);
		try {
			user = userRepository.save(user);
		} catch (DataIntegrityViolationException de) {
			throw de;
		}

		catch (Exception e) {
			throw e;
		}

		return user;

	}


	@Override
	public User getUserFromToken(String token) throws UserIsNotFoundException {
		Claims claims = jwtUtils.verifyJWT(token);
		if (claims != null) {
			String username = claims.getSubject();
			Optional<User> optionalUser = userRepository.findByUsername(username);
			if (optionalUser.isPresent()) {
				return optionalUser.get();
			}
		}
		throw new UserIsNotFoundException();
	}

	@Override
	public boolean changePassword(String username, String password) throws Exception {
		int resultCount = userRepository.updatePassword(username, password);
		return resultCount > 0;
	}

	@Override
	public boolean validateSignIn(String token) {
		try {
			Claims claims = jwtUtils.verifyJWT(token);
			if (claims != null) {
				return true;
			}
		} catch (Exception e) {

			throw e;
		}
		return false;
	}

	@Override
	public String getSecretQuestionAnswer(String username) {
		Optional<User> user = userRepository.findByUsername(username);
		if (user.get().isActive()) {
			return user.get().getSecretAnswer();
		}
		return null;
	}

	public static TokenService getTokenService() {

		ServiceLoader<TokenService> serviceLoader = ServiceLoader.load(TokenService.class);
		for (TokenService provider : serviceLoader) {
			return provider;
		}
		throw new NoClassDefFoundError("Unable to load a driver " + TokenService.class.getName());
	}

	@Override
	public User getUser(long id) throws UserIsNotFoundException {
		Optional<User> user = userRepository.findById(id);
		if (user.isPresent()) {
			return user.get();
		}

		// Handle the case where the user is not found
		throw new UserIsNotFoundException("User with id " + id + " not found");
	}

	@Override
	public boolean resetPassword(String username, String secretAnswer, String newPassword) throws Exception {

		Optional<User> user = userRepository.findByUsername(username);

		if (!user.isPresent()) {
			throw new UserIsNotFoundException("User not found");
		}
		if (!user.get().getSecretAnswer().equals(secretAnswer)) {
			throw new IncorrectCredentialsException("Incorrect secret answer");
		}

		user.get().setPassword(newPassword);
		userRepository.save(user.get());

		return true;
	}

	@Override
	public void setDeviceRegistrationToken(long id, String deviceRegistrationToken) throws UserIsNotFoundException {

		Optional<User> user = userRepository.findById(id);

		if (user.isPresent()) {
			user.get().setDeviceRegistrationToken(deviceRegistrationToken);
			userRepository.save(user.get());
		} else {
			throw new UserIsNotFoundException("User with id " + id + " not found");
		}

	}


}
