package martins.developer.world.jpa.web;

import martins.developer.world.ejb.MyRepository;
import martins.developer.world.jpa.ChildEntity;
import martins.developer.world.jpa.RootEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@ConversationScoped
@Named
public class HomeBackingBean implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeBackingBean.class);
    @EJB
    private MyRepository myRepository;
    @Inject
    private Conversation conversation;
    private long idToDelete = 1;
    List<RootEntity> rootEntities = new LinkedList<RootEntity>();

    @PostConstruct
    private void postConstruct() {
        conversation.begin();
    }

    public void persist() {
        List<RootEntity> rootEntityList = new LinkedList<RootEntity>();
        RootEntity rootEntity = createRootEntity();
        rootEntityList.add(rootEntity);
        myRepository.persist(rootEntityList);
    }

    private RootEntity createRootEntity() {
        RootEntity rootEntity = new RootEntity();
        rootEntity.setField1("field1");
        rootEntity.setField2("field2");
        ChildEntity childEntity = createChildEntity(rootEntity);
        rootEntity.getChildEntities().add(childEntity);
        return rootEntity;
    }

    private ChildEntity createChildEntity(RootEntity rootEntity) {
        ChildEntity childEntity = new ChildEntity();
        childEntity.setField1("field1");
        childEntity.setField2("field2");
        childEntity.setRootEntity(rootEntity);
        return childEntity;
    }

    private void loadAll() {
        rootEntities = myRepository.loadAll();
        for (RootEntity rootEntity : rootEntities) {
            LOGGER.debug(String.format("Loaded RootEntity with id %s.", rootEntity.getId()));
            Set<ChildEntity> childEntities = rootEntity.getChildEntities();
            for(ChildEntity childEntity : childEntities) {
                LOGGER.debug(String.format("Loaded ChildEntity with id %s.", childEntity.getId()));
            }
        }
    }

    public void delete() {
        LOGGER.debug(String.format("Trying to delete RootEntity with id %s.", idToDelete));
        myRepository.deleteById(idToDelete);
    }

    public void remove() {
        LOGGER.debug(String.format("Trying to remove RootEntity with id %s.", idToDelete));
        myRepository.removeById(idToDelete);
    }

    public long getIdToDelete() {
        return idToDelete;
    }

    public void setIdToDelete(long idToDelete) {
        this.idToDelete = idToDelete;
    }

    public List<RootEntity> getRootEntities() {
        return rootEntities;
    }

    public void setRootEntities(List<RootEntity> rootEntities) {
        this.rootEntities = rootEntities;
    }
}
