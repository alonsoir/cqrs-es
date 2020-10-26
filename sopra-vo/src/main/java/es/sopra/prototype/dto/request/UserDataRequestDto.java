package es.sopra.prototype.dto.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDataRequestDto {

    private String name;
    private String dateRegister;
}
