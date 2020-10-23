package es.sopra.prototype.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import es.sopra.prototype.services.UserDataService;
import es.sopra.prototype.vo.UserData;

@RestController
public class UserDataController implements UserDataApi {

	private static final Logger LOG = LogManager.getLogger(UserDataController.class);

	private UserDataService userDataService;

	@Autowired
	public void setUserDataService(UserDataService userDataService) {
		this.userDataService = userDataService;
	}

	@Override
	public UserData getProduct(@PathVariable Long id) {

		return userDataService.getById(id);
	}

}
