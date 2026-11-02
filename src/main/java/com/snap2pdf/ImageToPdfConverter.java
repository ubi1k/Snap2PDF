
package com.snap2pdf;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class ImageToPdfConverter {

    public void convert(List<File> images, File outputFile)
            throws IOException {

        if (images == null || images.isEmpty()) {
            throw new IllegalArgumentException(
                "Please select at least one image."
            );
        }

        try (PDDocument document = new PDDocument()) {

            for (File image : images) {

                String name = image.getName().toLowerCase();

                if (!name.endsWith(".jpg") &&
                    !name.endsWith(".jpeg")) {

                    throw new IllegalArgumentException(
                        "Only JPG and JPEG images are supported."
                    );
                }

                PDImageXObject pdfImage =
                    PDImageXObject.createFromFileByContent(
                        image, document
                    );

                float width = pdfImage.getWidth();
                float height = pdfImage.getHeight();

                PDRectangle pageSize =
                    new PDRectangle(width, height);

                PDPage page = new PDPage(pageSize);

                document.addPage(page);

                try (PDPageContentStream content =
                         new PDPageContentStream(document, page)) {

                    content.drawImage(
                        pdfImage, 0, 0, width, height
                    );
                }
            }

            document.save(outputFile);
        }
    }
}
