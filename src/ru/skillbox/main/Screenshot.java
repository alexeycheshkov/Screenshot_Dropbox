package ru.skillbox.main;

import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Screenshot implements Runnable{

    private DbxClientV2 client;
    private BufferedImage image;

    public Screenshot(DbxClientV2 client, BufferedImage image) {
        this.client = client;
        this.image = image;
    }


    @Override
    public void run() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image,"png",baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date nowDay= new Date();

        try (InputStream in = bais) {
            FileMetadata metadata = client.files().uploadBuilder("/img_"+format.format(nowDay)+".png")
                    .uploadAndFinish(in);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                bais.close();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
