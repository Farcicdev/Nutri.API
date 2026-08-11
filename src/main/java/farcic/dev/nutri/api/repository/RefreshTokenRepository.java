package farcic.dev.nutri.api.repository;

import farcic.dev.nutri.api.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @EntityGraph(attributePaths = "nutricionista")
    Optional<RefreshToken> findByTokenHash(String tokenHash);
}
