package technology.tikal.customers.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

public class Group {

    @NotNull
    @Length(min=3, max=26)
    @Pattern(regexp="\\w*")
    private String name;
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }    
}
