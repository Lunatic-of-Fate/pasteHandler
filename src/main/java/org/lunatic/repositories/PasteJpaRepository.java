package org.lunatic.repositories;

import org.lunatic.models.Paste;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasteJpaRepository extends JpaRepository<Paste, Long> {
}
