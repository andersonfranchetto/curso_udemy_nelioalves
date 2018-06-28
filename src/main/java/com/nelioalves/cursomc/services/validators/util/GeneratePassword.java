package com.nelioalves.cursomc.services.validators.util;

import java.util.Random;

public class GeneratePassword {

    private static Random random = new Random();

    public static String generatePassword(){
        char[] vet = new char[10];
        for(int i=0; i<vet.length ;i++){
            vet[i] = randomChar();
        }
        return new String(vet);
    }

    private static char randomChar(){
        int opt = random.nextInt(3);

        if(opt == 0){ //gera 1 digito
            return (char) (random.nextInt(10) + 48);
        } else if(opt == 1){//gera 1 letra MAIUSCULA
            return (char) (random.nextInt(26) + 65);
        } else { //gera 1 letra minuscula
            return (char) (random.nextInt(26) + 97);
        }
    }
}
