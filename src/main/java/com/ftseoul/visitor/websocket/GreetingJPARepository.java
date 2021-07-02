package com.ftseoul.visitor.websocket;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GreetingJPARepository extends JpaRepository<Greeting, Long> {

}
