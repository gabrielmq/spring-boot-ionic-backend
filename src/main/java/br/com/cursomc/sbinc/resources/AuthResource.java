package br.com.cursomc.sbinc.resources;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.cursomc.sbinc.domain.dto.EmailDTO;
import br.com.cursomc.sbinc.security.JWTUtils;
import br.com.cursomc.sbinc.security.UserSS;
import br.com.cursomc.sbinc.services.AuthService;
import br.com.cursomc.sbinc.services.UserService;

@RestController
@RequestMapping(value = "/auth")
public class AuthResource {

	@Autowired
	private JWTUtils jwtUtil;
	
	@Autowired
	private AuthService service;
	
	@RequestMapping(value = "/refresh_token", method = RequestMethod.POST) 
	public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
		UserSS user = UserService.authenticated();
		String token = jwtUtil.generateToken(user.getUsername());
		response.addHeader("Authorization", "Bearer "+token);
		response.addHeader("access-control-expose-headers", "Authorization");
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value = "/forgot", method = RequestMethod.POST) 
	public ResponseEntity<Void> forgot(@Valid @RequestBody EmailDTO emailDto) {
		service.sendNewPassword(emailDto.getEmail());
		return ResponseEntity.noContent().build();
	}
}
