package com.nelioalves.cursomc.services;

import com.nelioalves.cursomc.services.exceptions.FileException;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class ImageService {

    public BufferedImage getJpgImageFromFile(MultipartFile multipartFile){
        String extensao = FilenameUtils.getExtension(multipartFile.getOriginalFilename());

        if(!"png".equals(extensao) && !"jpg".equals(extensao))
            throw new FileException("Somente imagens PNG e JPG são permitidas");

        try {
            BufferedImage imagem = ImageIO.read(multipartFile.getInputStream());

            if("png".equals(extensao))
                imagem = pngToJpg(imagem);

            return imagem;
        } catch (IOException e){
            throw new FileException("Erro ao ler arquivo");
        }
    }

    public BufferedImage pngToJpg(BufferedImage imagem) {
        BufferedImage jpgImage = new BufferedImage(imagem.getWidth(), imagem.getHeight(), BufferedImage.TYPE_INT_ARGB);
        jpgImage.createGraphics().drawImage(imagem, 0, 0, Color.white, null);
        return jpgImage;
    }

    public InputStream getInputStream(BufferedImage bufferedImage, String extension){
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, extension, byteArrayOutputStream);
            return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        }catch (IOException e){
            throw new FileException("Erro ao ler aquivo");
        }
    }

    public BufferedImage cropSquare(BufferedImage sourceImg){
        int min = (sourceImg.getHeight() <= sourceImg.getWidth()) ? sourceImg.getHeight() : sourceImg.getWidth();

        return Scalr.crop(sourceImg,
                         (sourceImg.getWidth()/2) - (min/2),
                         (sourceImg.getHeight()/2) - (min/2),
                         min,
                         min);
    }

    public BufferedImage resize(BufferedImage sourceImg, int size){
        return Scalr.resize(sourceImg, Scalr.Method.ULTRA_QUALITY, size);
    }
}
