package entityportal.service.model;

import lombok.Data;

@Data
public class PasswordResetRequest {
    String userName;
    String oldPassword;
    String newPassword;
}
