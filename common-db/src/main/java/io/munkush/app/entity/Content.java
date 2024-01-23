package io.munkush.app.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "contents")
public class Content {

    @Id
    private String id;

    private String name;

    private String contentType;
    private String url;

    @Builder.Default
    private LocalDateTime dateTime = LocalDateTime.now();
}
