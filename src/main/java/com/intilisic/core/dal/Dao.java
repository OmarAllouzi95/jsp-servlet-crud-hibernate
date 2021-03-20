package com.intilisic.core.dal;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface Dao<T> {

	Optional<T> get(long id) throws IOException;

	List<T> getAll() throws IOException;

	void save(T t) throws IOException;

	void update(T t) throws IOException;

	void delete(long id) throws IOException;
}
