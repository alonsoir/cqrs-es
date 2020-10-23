package es.sopra.prototype.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.sopra.prototype.repositories.UserDataRepository;
import es.sopra.prototype.services.UserDataService;
import es.sopra.prototype.vo.UserData;

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
	public UserData getById(Long id) {
		return userDataRepository.findById(id).orElse(null);
	}

	@Override
	public UserData saveOrUpdate(UserData UserData) {
		userDataRepository.save(UserData);
		return UserData;
	}

	@Override
	public void delete(Long id) {
		userDataRepository.deleteById(id);

	}

}
