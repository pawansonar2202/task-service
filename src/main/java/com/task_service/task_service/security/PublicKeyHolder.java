package com.task_service.task_service.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPublicKey;

@Component
@Getter
@Setter
public class PublicKeyHolder {

    private RSAPublicKey rsaPublicKey;

}
