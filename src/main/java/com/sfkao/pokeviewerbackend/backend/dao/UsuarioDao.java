package com.sfkao.pokeviewerbackend.backend.dao;

import com.sfkao.pokeviewerbackend.backend.modelo.Usuario;
import com.sfkao.pokeviewerbackend.backend.modelo.UsuarioSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public Usuario getUsuarioByApikey(String apikey){
        List<UsuarioSQL> list = jdbcTemplate.query("SELECT * FROM Usuario WHERE apikey = ?", (rs, rw) -> {
            return new UsuarioSQL(rs.getString("username"),rs.getString("email"),
                    rs.getString("saltedHash"),rs.getString("salt"),rs.getString("apikey"),rs.getInt("pk1"),rs.getInt("pk2"),rs.getInt("pk3"));
        },apikey);
        if(list.size()==0)
            return null;
        UsuarioSQL usuarioSQL = list.get(0);
        return usuarioSQL.toUser();
    }

    public void cargarLikesYFavsDeUsuario(Usuario u){
        u.setLikes(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM Likes WHERE id IN (SELECT id FROM Equipo WHERE Equipo.usernameAutor = ? )",Integer.class,u.getUsername()));
        u.setFavoritos(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM Favoritos WHERE id IN (SELECT id FROM Equipo WHERE Equipo.usernameAutor = ? )",Integer.class,u.getUsername()));
    }

}
