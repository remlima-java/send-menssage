package br.dev.rtisystem.service.interfaces.auth;

import br.dev.rtisystem.model.dtos.login.AuthResponseDto;
import br.dev.rtisystem.model.dtos.login.LoginDto;
import br.dev.rtisystem.model.dtos.login.RegisterDto;

public interface AuthService {
    AuthResponseDto register(RegisterDto request);
    AuthResponseDto login(LoginDto request);

}
