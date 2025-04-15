package br.org.apae.api_crud_de_grupos.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.org.apae.api_crud_de_grupos.model.Grupo;

public interface APIGruposRepository extends JpaRepository<Grupo, UUID>{

}
