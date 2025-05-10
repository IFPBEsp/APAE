package br.edu.ifpb.esp.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.esp.demo.domain.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
