package sopra.prototype.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sopra.prototype.repositories.UserDataRepository;
import sopra.prototype.vo.UserData;

import java.util.ArrayList;
import java.util.List;

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
	@Transactional
	public UserData saveOrUpdate(UserData UserData) {
		userDataRepository.save(UserData);
		return UserData;
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		userDataRepository.deleteById(id);

	}

	@Override
	public List<UserData> findByName(String name) {
		return userDataRepository.findByName(name);
	}

}
