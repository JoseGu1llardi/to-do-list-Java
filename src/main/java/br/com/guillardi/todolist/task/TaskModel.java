package br.com.guillardi.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "tasks")
public class TaskModel {
    private enum TaskPriority {
        LOW("Low"),
        MEDIUM("Medium"),
        HIGH("High");

        private String description;

        TaskPriority(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return description;
        }

        public static final TaskPriority DEFAULT = LOW;
    }

    private enum TaskStatus {
        PENDING("PENDING"),
        IN_PROGRESS("In progress"),
        DONE("Done");

        private String description;

        TaskStatus(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return description;
        }

        public static final TaskStatus DEFAULT = PENDING;
    }

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private UUID authorId;

    @Column(length = 50)
    private String title;
    private String description;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private TaskPriority priority = TaskPriority.DEFAULT;
    private TaskStatus status = TaskStatus.DEFAULT;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public void setTitle(String title) throws Exception {
        if (title.length() > 50) {
            throw new Exception("The field title must contain a maximum of 50 characters!");
        }

        this.title = title;
    }
}
