
package com.snap2pdf;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;

import javax.imageio.ImageIO;

import static org.junit.jupiter.api.Assertions.*;

class ImageToPdfConverterTest {

    @TempDir
    Path tempDir;

    private File createTestImage(String name) throws IOException {

        File imageFile = tempDir.resolve(name).toFile();

        BufferedImage image = new BufferedImage(
            200, 200, BufferedImage.TYPE_INT_RGB
        );

        ImageIO.write(image, "jpg", imageFile);

        return imageFile;
    }

    @Test
    void testSingleImageConversion() throws Exception {

        File image = createTestImage("test1.jpg");
        File output = tempDir.resolve("single.pdf").toFile();

        ImageToPdfConverter converter =
            new ImageToPdfConverter();

        converter.convert(
            Collections.singletonList(image), output
        );

        assertTrue(output.exists());
        assertTrue(output.length() > 0);

        try (PDDocument pdf = Loader.loadPDF(output)) {
            assertEquals(1, pdf.getNumberOfPages());
        }
    }

    @Test
    void testMultipleImageConversion() throws Exception {

        File image1 = createTestImage("test1.jpg");
        File image2 = createTestImage("test2.jpg");
        File image3 = createTestImage("test3.jpg");

        File output = tempDir.resolve("multiple.pdf").toFile();

        ImageToPdfConverter converter =
            new ImageToPdfConverter();

        converter.convert(
            Arrays.asList(image1, image2, image3), output
        );

        try (PDDocument pdf = Loader.loadPDF(output)) {
            assertEquals(3, pdf.getNumberOfPages());
        }
    }

    @Test
    void testEmptyImageList() {

        ImageToPdfConverter converter =
            new ImageToPdfConverter();

        File output = tempDir.resolve("empty.pdf").toFile();

        assertThrows(
            IllegalArgumentException.class,
            () -> converter.convert(
                Collections.emptyList(), output
            )
        );
    }

    @Test
    void testUnsupportedFileFormat() throws Exception {

        File image = tempDir.resolve("test.png").toFile();
        File output = tempDir.resolve("invalid.pdf").toFile();

        ImageToPdfConverter converter =
            new ImageToPdfConverter();

        assertThrows(
            IllegalArgumentException.class,
            () -> converter.convert(
                Collections.singletonList(image), output
            )
        );
    }
}
