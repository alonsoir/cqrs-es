package sopra.prototype.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import sopra.prototype.command.handler.services.ServiceCommandHandler;
import sopra.prototype.config.services.CommandConfig;
import sopra.prototype.config.services.CommandHandlerConfig;
import sopra.prototype.services.impl.utils.SopraUtils;
import sopra.prototype.vo.UserData;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

// TODO no me está instanciando ServiceCommandHandler, de hecho, el test no me dice absolutamente nada.

@SpringBootTest(classes={CommandHandlerConfig.class})
@Slf4j
public class ServiceCommandHandlerTest {
/*
    @Autowired
    private ServiceCommandHandler serviceCommandHandler;

    @Test
    private void saveOrUpdateIntoDB(){

        // GIVEN
        UserData user = new UserData();
        String name = "Papa";
        user.setName(name);
        user.setDateRegister(SopraUtils.getActualFormatedDate());
        //WHEN
        boolean isUserSaved = serviceCommandHandler.saveOrUpdateIntoDB(user);
        // THEN
        assertTrue(isUserSaved,"isUser should be true");
    }
    @Test
    private void deleteFromDB(){
        // GIVEN
        UserData user = new UserData();
        String name = "Mama";
        user.setName(name);
        user.setDateRegister(SopraUtils.getActualFormatedDate());

        // WHEN
        boolean saved = serviceCommandHandler.saveOrUpdateIntoDB(user);
        assertTrue(saved,"saved should be true");

        // THEN
        List<UserData> listUsers = serviceCommandHandler.findByName(name);
        assertNotNull(listUsers,"listUsers should not be null");
        assertTrue(listUsers.size()>0,"listUsers size should be greater than zero");
        assertTrue(listUsers.size()==1,"listUsers size should be only one");
        UserData userRecovered = listUsers.get(0);
        assertNotNull(userRecovered,"userRecovered should not be null");
        boolean isDeleted = serviceCommandHandler.deleteFromDB(userRecovered.getIdUserData());
        assertTrue(isDeleted,"isDeleted should be true");
    }

    @Test
    private void listAll(){
        List<UserData> list = serviceCommandHandler.listAll();
        assertNotNull(list,"list should not null");
        assertTrue(list.size()>0,"list size should be greater than zero.");
    }
/*
    En realidad este método debería ser privado...
    @Test
    private void pushIntoEventStore(){

        String message = "User " + id + " was deleted at " + LocalDate.now();
        CommandMessage message = CommandMessage.builder()
                .timestamp(System.currentTimeMillis())
                .message(message)
                .dateRegister(formattedString)
                .name(String.valueOf(id))
                .build();

        serviceCommandHandler.pushIntoEventStore(message);

    }

 */
}
