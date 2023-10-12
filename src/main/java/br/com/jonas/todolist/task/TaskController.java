package br.com.jonas.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jonas.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskRepository repository;

    @PostMapping
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        // Pegando o idUser da requisição Http, pois já está sendo passado no header
        taskModel.setIdUser((UUID) request.getAttribute("idUser"));

        // Validar a data e hora
        var currentDate = LocalDateTime.now();
        // Valida as datas conforme a data atual
        if (currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter((taskModel.getEndAt()))) {
            return ResponseEntity.status(400).body("A data de início/término deve ser maior que a data atual");
        }
        // Valida se a data de início é menor que a data de término
        if (taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(400).body("A data de início deve ser menor que a data de término");
        }

        var taskCreated = repository.save(taskModel);
        return ResponseEntity.status(201).body(taskCreated);

    }

    @GetMapping
    public ResponseEntity<List<TaskModel>> findAll(HttpServletRequest request) {
        var tasks = repository.findByIdUser((UUID) request.getAttribute("idUser"));
        return ResponseEntity.status(200).body(tasks);
    }

    // localhost:8080/tasks/1234563216549-bv-da123
    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id) {
        var idUser = request.getAttribute("idUser");
        // Vai buscar a task pelo ID, caso não achar vai retornar null
        var task = repository.findById(id).orElse(null);
        // Vai mesclar os dados vazios com os já existentes
        Utils.copyNonNullProperties(taskModel, task);
        // Vai salvar a task com os dados já mesclados
        return ResponseEntity.status(200).body(repository.save(task));
    }
}
