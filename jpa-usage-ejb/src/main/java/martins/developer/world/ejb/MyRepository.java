package martins.developer.world.ejb;

import martins.developer.world.jpa.RootEntity;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class MyRepository {
    @PersistenceContext
    private EntityManager entityManager;
    @Inject
    private DeletionHelper deletionHelper;

    public void persist(List<RootEntity> rootEntities) {
        for(RootEntity rootEntity : rootEntities) {
            entityManager.persist(rootEntity);
        }
    }

    public List<RootEntity> loadAll() {
        return entityManager.createQuery("select r from RootEntity r", RootEntity.class).getResultList();
    }

    public void deleteById(long id) {
        /*
List<Long> childIds = entityManager.createQuery("select c.id from ChildEntity c where c.rootEntity.id = :pid").setParameter("pid", id).getResultList();
for(Long childId : childIds) {
    entityManager.createQuery("delete from ChildEntity c where c.id = :id").setParameter("id", childId).executeUpdate();
}
entityManager.createQuery("delete from RootEntity r where r.id = :id").setParameter("id", id).executeUpdate();
        */
        deletionHelper.delete(entityManager, RootEntity.class, id);
    }

    public void removeById(long id) {
        RootEntity rootEntity = entityManager.getReference(RootEntity.class, id);
        entityManager.remove(rootEntity);
    }
}
