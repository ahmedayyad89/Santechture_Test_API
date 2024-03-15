package com.santechture.api.repository;

import com.santechture.api.entity.Token;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query(" select t From Token t inner join Admin a on t.admin.adminId = a.adminId where a.adminId = :adminId and t.isLoggedOut = false ")
    List<Token> findAllTokenByAdminId(Integer adminId);


    Optional<Token> findByToken(String token);
}
