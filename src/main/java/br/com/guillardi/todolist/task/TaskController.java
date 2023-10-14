package br.com.guillardi.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import br.com.guillardi.todolist.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        var authorId = request.getAttribute("authorId");
        taskModel.setAuthorId((UUID) authorId);

        var currentDate = LocalDateTime.now();
        if (currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())) {
            return ResponseEntity.badRequest()
                    .body("The start/end date must be equal to or greater than the current date.");
        } else if (taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
            return ResponseEntity.badRequest()
                    .body("The date of the beginning must be less than the date of the end.");
        }

        var task = this.taskRepository.save(taskModel);

        return ResponseEntity.ok().body(task);
    }

    @GetMapping("/")
    public List<TaskModel> listTasks(HttpServletRequest request) {
        var authorId = request.getAttribute("authorId");

        return this.taskRepository.findByAuthorId((UUID) authorId);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID taskId) {
        var task = this.taskRepository.findById(taskId).orElse(null);

        if(task == null) {
            return ResponseEntity.badRequest().body("Task not found");
        }

        var authorId = request.getAttribute("authorId");

        if (!task.getAuthorId().equals(authorId)) {
            return ResponseEntity.badRequest().body("Not allowed!");
        }

        Utils.copyNonNullProperties(taskModel, task);

        var taskUpdated = this.taskRepository.save(task);

        return ResponseEntity.ok().body(taskUpdated);
    }
}
