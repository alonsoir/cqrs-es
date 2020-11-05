package sopra.prototype.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sopra.prototype.repositories.UserDataRepository;
import sopra.prototype.services.UserDataService;
import sopra.prototype.vo.UserData;

/**
 * Created by jt on 1/10/17.
 */
@Service
public class UserDataServiceImpl implements UserDataService {

	private UserDataRepository userDataRepository;

	@Autowired
	public UserDataServiceImpl(UserDataRepository userDataRepository) {

		this.userDataRepository = userDataRepository;
	}

	@Override
	public List<UserData> listAll() {
		List<UserData> UserDatas = new ArrayList<>();
		userDataRepository.findAll().forEach(UserDatas::add);
		return UserDatas;
	}

	@Override
	public UserData getById(Integer id) {
		return userDataRepository.findById(id).orElse(null);
	}

	@Override
	public UserData saveOrUpdate(UserData UserData) {
		userDataRepository.save(UserData);
		return UserData;
	}

	@Override
	public void delete(Integer id) {
		userDataRepository.deleteById(id);

	}

}
