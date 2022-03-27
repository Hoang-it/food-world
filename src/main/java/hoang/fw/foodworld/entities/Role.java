package hoang.fw.foodworld.entities;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    public Integer getId() {
        return id;
    }
    public Role(){

    }
    public Role(String name) {
        this.name = name;
    }
// remaining getters and setters

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}