package es.sopra.prototype.services.mapper;

import es.sopra.prototype.dto.response.UserDataResponseDto;
import es.sopra.prototype.vo.UserData;
import org.springframework.stereotype.Component;

@Component
public class UserDataMapper {

    public UserDataResponseDto userDataToUserDataResponseDto(UserData userData) {
        return UserDataResponseDto
                .builder()
                .dateRegister(userData.getDateRegister())
                .idUserData(userData.getIdUserData())
                .name(userData.getName())
                .build();
    }
}
