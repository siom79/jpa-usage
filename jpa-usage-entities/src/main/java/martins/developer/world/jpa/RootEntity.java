package martins.developer.world.jpa;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "ROOT_ENTITY")
public class RootEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "root_entity_seq")
    @SequenceGenerator(name = "root_entity_seq", sequenceName = "ROOT_ENTITY_ID_SEQ", allocationSize = 1)
    private long id;

@OneToMany(mappedBy = "rootEntity", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
private Set<ChildEntity> childEntities = new HashSet<ChildEntity>(0);

    private String field1;
    private String field2;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<ChildEntity> getChildEntities() {
        return childEntities;
    }

    public void setChildEntities(Set<ChildEntity> childEntities) {
        this.childEntities = childEntities;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }
}
