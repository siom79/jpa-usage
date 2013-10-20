package martins.developer.world.ejb;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class DeletionHelper {

public void delete(EntityManager entityManager, Class parentClass, Object parentId) {
    Field idField = getIdField(parentClass);
    if (idField != null) {
        List<Field> oneToManyFields = getOneToManyFields(parentClass);
        for (Field field : oneToManyFields) {
            Class childClass = getFirstActualTypeArgument(field);
            if (childClass != null) {
                Field manyToOneField = getManyToOneField(childClass, parentClass);
                Field childClassIdField = getIdField(childClass);
                if (manyToOneField != null && childClassIdField != null) {
                    List<Long> childIds = entityManager.createQuery(String.format("select c.%s from %s c where c.%s.%s = :pid", childClassIdField.getName(), childClass.getSimpleName(), manyToOneField.getName(), idField.getName())).setParameter("pid", parentId).getResultList();
                    for (Long childId : childIds) {
                        entityManager.createQuery(String.format("delete from %s c where c.%s = :id", childClass.getSimpleName(), childClassIdField.getName())).setParameter("id", childId).executeUpdate();
                    }
                }
            }
        }
        entityManager.createQuery(String.format("delete from %s e where e.%s = :id", parentClass.getSimpleName(), idField.getName())).setParameter("id", parentId).executeUpdate();
    }
}

    private Class getFirstActualTypeArgument(Field field) {
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            if (typeArguments.length > 0) {
                Class<?> childClass = (Class<?>) typeArguments[0];
                return childClass;
            }
        }
        return null;
    }

    private Field getManyToOneField(Class clazz, Class parentClass) {
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(ManyToOne.class)) {
                Class<?> type = field.getType();
                if (parentClass.equals(type)) {
                    return field;
                }
            }
        }
        return null;
    }

    private Field getIdField(Class clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(Id.class)) {
                return field;
            }
        }
        return null;
    }

    private List<Field> getOneToManyFields(Class clazz) {
        List<Field> fields = new LinkedList<Field>();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(OneToMany.class)) {
                fields.add(field);
            }
        }
        return fields;
    }
}