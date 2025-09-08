package com.relationshipcenter.shared;

import java.util.Collection;

public interface RepositoryGen <T, ID> {

    void create(T model);

    Collection<T> list();
}
