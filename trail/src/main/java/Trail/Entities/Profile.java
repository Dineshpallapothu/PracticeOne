package Trail.Entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name = "profile1")
@Data
public class Profile {
    @Id
    @Column(name = "id")
   // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int id;
    //@NotBlank
    @Column(name = "name")
    private String name;
   // @NotBlank
    @Column(name = "password")
    private String password;
   // @NotBlank
    @Column(name = "email")
    private String email;
   // @NotBlank
    @Column(name = "mobile")
    private String mobile;
    //@NotBlank
    @Column(name = "role")
    private String role;
}
