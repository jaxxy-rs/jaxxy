package org.jaxxy.rs.util.reflect;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;

import org.apache.commons.lang3.reflect.TypeUtils;

public class Types {
//----------------------------------------------------------------------------------------------------------------------
// Static Methods
//----------------------------------------------------------------------------------------------------------------------

    public static <T> T instantiate(Class<T> type) {
        try {
            return type.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new ReflectionException(e, "Unable to instantiate object of type %s.", type.getCanonicalName());
        }
    }

    public static <T extends P, P, C extends Type> C typeParamFromClass(Class<T> concreteClass, Class<P> definingClass, int varIndex) {
        return typeParamFromType(concreteClass, definingClass, varIndex);
    }

    @SuppressWarnings("unchecked")
    public static <P, C extends Type> C typeParamFromType(Type type, Class<P> definingClass, int varIndex) {
        Map<TypeVariable<?>, Type> typeArguments = TypeUtils.getTypeArguments(type, definingClass);
        TypeVariable<Class<P>> typeVar = definingClass.getTypeParameters()[varIndex];
        return (C) typeArguments.get(typeVar);
    }
}
