package martins.developer.world.jpa;

import javax.persistence.*;

@Entity
@Table(name = "CHILD_ENTITY")
public class ChildEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "child_entity_seq")
    @SequenceGenerator(name = "child_entity_seq", sequenceName = "CHILD_ENTITY_ID_SEQ", allocationSize = 1)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PARENT")
    private RootEntity rootEntity;

    private String field1;
    private String field2;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public RootEntity getRootEntity() {
        return rootEntity;
    }

    public void setRootEntity(RootEntity rootEntity) {
        this.rootEntity = rootEntity;
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
