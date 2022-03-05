package ru.sberServer.model;
import lombok.*;
import nonapi.io.github.classgraph.json.Id;





@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder


public class Client {

    @Id
    private String id;

    private Integer value;

    private String name;

}
