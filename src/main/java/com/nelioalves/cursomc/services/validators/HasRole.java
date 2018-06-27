package com.nelioalves.cursomc.services.validators;

import com.nelioalves.cursomc.domain.enums.PerfilCliente;
import com.nelioalves.cursomc.security.domain.User;
import com.nelioalves.cursomc.security.services.UserService;

public class HasRole {

    public static boolean hasRoleUser(Integer clienteId){
        User user = UserService.authenticated();
        if(user==null
                || !user.hasRole(PerfilCliente.ADMIN)
                && !clienteId.equals(user.getId())){
            return true;
        }
        return false;
    }
}
