package com.example.dswii_t3_diaz_cesar.controller;

import com.example.dswii_t3_diaz_cesar.model.response.ResponseFile;
import com.example.dswii_t3_diaz_cesar.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/file")
public class FileController {

    private final FileService fileService;

    // Directorio para la API /filesimages
    private static final Path FILES_IMAGES_DIRECTORY = Paths.get("images");

    // Extensiones permitidas y tamaño máximo para la API /filesimages
    private static final String FILES_IMAGES_ALLOWED_EXTENSIONS = "png";
    private static final long FILES_IMAGES_MAX_FILE_SIZE = 1048576;

    // Directorio para la API /filesexcel
    private static final Path FILESEXCEL_DIRECTORY = Paths.get("documents");

    // Extensiones permitidas y tamaño máximo para la API /filesexcel
    private static final String FILESEXCEL_ALLOWED_EXTENSIONS = "xlsx";
    private static final long FILESEXCEL_MAX_FILE_SIZE = 1572864;

    @PostMapping("/upload")
    public ResponseEntity<ResponseFile> subirArchivos(@RequestParam("files") List<MultipartFile> files) throws Exception {
        fileService.guardarArchivos(files);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseFile.builder().message("Los archivos fueron cargados correctamente").build());
    }

    @PostMapping("/filesimages")
    public ResponseEntity<ResponseFile> subirImagen(@RequestParam("file") MultipartFile file) {
        try {
            // Validar la extensión del archivo
            if (!Arrays.asList(FILES_IMAGES_ALLOWED_EXTENSIONS.split(",")).contains(getFileExtension(file))) {
                return new ResponseEntity<>(ResponseFile.builder().message("Solo se permiten archivos con las extensiones: " + FILES_IMAGES_ALLOWED_EXTENSIONS).build(), HttpStatus.BAD_REQUEST);
            }

            // Validar el tamaño máximo del archivo
            if (file.getSize() > FILES_IMAGES_MAX_FILE_SIZE) {
                return new ResponseEntity<>(ResponseFile.builder().message("El tamaño del archivo excede el límite permitido (" + FILES_IMAGES_MAX_FILE_SIZE + " bytes).").build(), HttpStatus.BAD_REQUEST);
            }

            // Guardar el archivo en el directorio Images
            fileService.guardarEnDirectorio(file, FILES_IMAGES_DIRECTORY);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseFile.builder().message("Imagen cargada correctamente").build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseFile.builder().message("Error al subir la imagen").build());
        }
    }

    @PostMapping("/filesexcel")
    public ResponseEntity<ResponseFile> subirExcel(@RequestParam("file") MultipartFile file) {
        try {
            // Validar la extensión del archivo
            if (!Arrays.asList(FILESEXCEL_ALLOWED_EXTENSIONS.split(",")).contains(getFileExtension(file))) {
                return new ResponseEntity<>(ResponseFile.builder().message("Solo se permiten archivos con las extensiones: " + FILESEXCEL_ALLOWED_EXTENSIONS).build(), HttpStatus.BAD_REQUEST);
            }

            // Validar el tamaño máximo del archivo
            if (file.getSize() > FILESEXCEL_MAX_FILE_SIZE) {
                return new ResponseEntity<>(ResponseFile.builder().message("El tamaño del archivo excede el límite permitido (" + FILESEXCEL_MAX_FILE_SIZE + " bytes).").build(), HttpStatus.BAD_REQUEST);
            }

            // Guardar el archivo en el directorio Documentos
            fileService.guardarEnDirectorio(file, FILESEXCEL_DIRECTORY);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseFile.builder().message("Archivo Excel cargado correctamente").build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseFile.builder().message("Error al subir el archivo Excel").build());
        }
    }

    // Método para obtener la extensión de un archivo
    private String getFileExtension(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        return originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf('.') + 1) : null;
    }
}
