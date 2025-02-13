package io.github.jchun247.collectables.repository.user;

import io.github.jchun247.collectables.model.user.UserPortfolio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPortfolioRepository extends JpaRepository<UserPortfolio, String>{
}