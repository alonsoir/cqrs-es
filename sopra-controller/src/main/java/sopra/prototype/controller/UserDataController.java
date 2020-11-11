package sopra.prototype.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import sopra.prototype.user.services.UserDataService;
import sopra.prototype.vo.UserData;

@RestController
public class UserDataController implements UserDataApi {

	// TODO faltan métodos por implementar, todos los del servicio de comandos y los del servicio de consultas,
	// aunque, al final, habrá un controller en cada proyecto.
	private static final Logger LOG = LogManager.getLogger(UserDataController.class);

	private UserDataService userDataCommandService;

	@Autowired
	public void setUserDataCommandService(UserDataService userDataCommandService) {
		this.userDataCommandService = userDataCommandService;
	}

	@Override
	public UserData getProduct(@PathVariable Integer id) {

		return userDataCommandService.getById(id);
	}

}
