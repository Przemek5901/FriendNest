package pl.polsl.friendnest.service.impl;

import org.springframework.stereotype.Service;
import pl.polsl.friendnest.exception.CustomException;
import pl.polsl.friendnest.service.FileService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String saveFileFromBase64(String base64, String path) {
        String[] parts = base64.split(",");

        String mimeType = parts[0].split(":")[1].split(";")[0];
        String base64Content = parts[1];

        String extension = "";
        if (mimeType.equals("image/png")) {
            extension = ".png";
        } else if (mimeType.equals("image/jpeg")) {
            extension = ".jpg";
        } else if (mimeType.equals("image/gif")) {
            extension = ".gif";
        } else {
            throw new CustomException("Nieobsługiwany typ pliku: " + mimeType);
        }

        String folderPath = "D:\\uploads\\" + path + "\\";
        File directory = new File(folderPath);

        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new CustomException("Nie udało się utworzyć folderu");
            }
        }

        String fileName = UUID.randomUUID().toString();
        String filePath = folderPath + fileName + extension;

        try {
            byte[] imageBytes = Base64.getDecoder().decode(base64Content);

            File outputFile = new File(filePath);
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                fos.write(imageBytes);
                return "http://localhost:8080/" + path + "/" + fileName + extension;
            }
        } catch (IllegalArgumentException | IOException e) {
            throw new CustomException("Nie udało się zapisać pliku");
        }
    }
}
