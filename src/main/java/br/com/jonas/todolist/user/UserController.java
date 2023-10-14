package br.com.jonas.todolist.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository repository;

    @PostMapping
    public ResponseEntity create(@RequestBody UserModel userModel) {
        // Validar se já tem algum username igual o que foi cadastrado
        var user = this.repository.findByUsername(userModel.getUsername());
        if(user != null) {
            return ResponseEntity.status(400).body("Usuário já existe");
        }

        // Hashear a senha:
        var passwordHashed = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
        // Mudar a password do user ou qualquer outro nome da entidade
        userModel.setPassword(passwordHashed);
        var userCreated = repository.save(userModel);
        return ResponseEntity.status(201).body(userCreated);
        
    }


}
