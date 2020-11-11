package sopra.prototype.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import sopra.prototype.config.services.CommandHandlerConfig;

// TODO no me está instanciando ServiceCommandHandler, de hecho, el test no me dice absolutamente nada.
// voy a tener que instanciar internamente cada una de las dependencias de ServiceCommandHandler...
@SpringBootTest(classes={CommandHandlerConfig.class})
@Slf4j
public class ServiceFAKECommandHandlerTest {
/*
    //@Autowired
    //private ServiceCommandHandler serviceCommandHandler;
    @Autowired
    private ServiceCommand serviceCommand;
    //@Autowired
    //private CommandServiceEventStore eventStore;

    @Test
    private void saveOrUpdateIntoDB(){

        // GIVEN
        UserData user = new UserData();
        String name = "Papa";
        user.setName(name);
        user.setDateRegister(SopraUtils.getActualFormatedDate());
        //WHEN
        boolean isUserSaved = serviceCommand.saveOrUpdateIntoDB(user);
        // THEN
        assertTrue(isUserSaved,"isUserSaved should be true");

        CommandMessage message = CommandMessage.builder()
                .timestamp(System.currentTimeMillis())
                .message("User " + user.getName() + " was created at " + LocalDate.now())
                .dateRegister(user.getDateRegister())
                .name(user.getName())
                .build();

        //boolean agregationCreationPushedToEventStore= eventStore.sendCommandMessage(message);
        //assertTrue(agregationCreationPushedToEventStore,"agregationCreationPushedToEventStore should be true");

    }
    @Test
    private void deleteFromDB(){
        // GIVEN
        UserData user = new UserData();
        String name = "Mama";
        user.setName(name);
        user.setDateRegister(SopraUtils.getActualFormatedDate());

        // WHEN
        boolean saved = serviceCommand.saveOrUpdateIntoDB(user);
        assertTrue(saved,"saved should be true");

        // THEN
        List<UserData> listUsers = serviceCommand.findByName(name);
        assertNotNull(listUsers,"listUsers should not be null");
        assertTrue(listUsers.size()>0,"listUsers size should be greater than zero");
        assertTrue(listUsers.size()==1,"listUsers size should be only one");
        UserData userRecovered = listUsers.get(0);
        assertNotNull(userRecovered,"userRecovered should not be null");
        boolean isDeleted = serviceCommand.deleteFromDB(userRecovered.getIdUserData());
        assertTrue(isDeleted,"isDeleted should be true");

        String message = "User " + userRecovered.getIdUserData() + " was deleted at " + LocalDate.now();
        CommandMessage commandMessage = CommandMessage.builder()
                .timestamp(System.currentTimeMillis())
                .message(message)
                .dateRegister(SopraUtils.getActualFormatedDate())
                .name(userRecovered.getName())
                .build();
        // boolean pushedToTopic =eventStore.sendCommandMessage(commandMessage);
        // assertTrue(pushedToTopic,"pushedToTopic should be true");

    }

    @Test
    private void listAll(){
        List<UserData> list = serviceCommand.listAll();
        assertNotNull(list,"list should not null");
        assertTrue(list.size()>0,"list size should be greater than zero.");
    }
*/
}
