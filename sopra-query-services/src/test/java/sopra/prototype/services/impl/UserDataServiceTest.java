package sopra.prototype.services.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sopra.prototype.services.UserDataService;
import sopra.prototype.services.impl.utils.SopraUtils;
import sopra.prototype.vo.UserData;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes={sopra.prototype.services.config.QueryConfig.class})
public class UserDataServiceTest {

    @Autowired
    private UserDataService userData;

    @Test
    public void whenSavingUser(){
        UserData user = new UserData();
        user.setName("Alonso");
        user.setDateRegister(SopraUtils.getActualFormatedDate());
        UserData saved = userData.saveOrUpdate(user);
        assertNotNull(saved,"saved should not be null");
    }


}
