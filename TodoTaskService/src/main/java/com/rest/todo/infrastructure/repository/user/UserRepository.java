package com.rest.todo.infrastructure.repository.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<com.rest.todo.infrastructure.repository.user.UserJPA, Long> {
}
