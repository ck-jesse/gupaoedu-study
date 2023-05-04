package com.coy.gupaoedu.study;

import com.theokanning.openai.image.CreateImageEditRequest;
import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.image.CreateImageVariationRequest;
import com.theokanning.openai.image.Image;
import com.theokanning.openai.service.OpenAiService;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author chenck
 * @date 2023/5/4 13:58
 */
public class ImageTest {

    static String filePath = "src/test/resources/penguin.png";
    static String fileWithAlphaPath = "src/test/resources/penguin_with_alpha.png";
    static String maskPath = "src/test/resources/mask.png";

    String openAiKey = "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";
    OpenAiService service = new OpenAiService(openAiKey, Duration.ofSeconds(30));

    @Test
    void createImageUrl() {
        CreateImageRequest createImageRequest = CreateImageRequest.builder()
                .prompt("penguin")
                .n(3)
                .size("256x256")
                .user("testing")
                .build();

        List<Image> images = service.createImage(createImageRequest).getData();
        assertEquals(3, images.size());
        assertNotNull(images.get(0).getUrl());
        for (Image image : images) {
            System.out.println(image.getUrl());
        }
    }

    @Test
    void createImageBase64() {
        CreateImageRequest createImageRequest = CreateImageRequest.builder()
                .prompt("penguin")
                .responseFormat("b64_json")
                .user("testing")
                .build();

        List<Image> images = service.createImage(createImageRequest).getData();
        assertEquals(1, images.size());
        assertNotNull(images.get(0).getB64Json());
    }

    @Test
    void createImageEdit() {
        CreateImageEditRequest createImageRequest = CreateImageEditRequest.builder()
                .prompt("a penguin with a red background")
                .responseFormat("url")
                .size("256x256")
                .user("testing")
                .n(2)
                .build();

        List<Image> images = service.createImageEdit(createImageRequest, fileWithAlphaPath, null).getData();
        assertEquals(2, images.size());
        assertNotNull(images.get(0).getUrl());
    }

    @Test
    void createImageEditWithMask() {
        CreateImageEditRequest createImageRequest = CreateImageEditRequest.builder()
                .prompt("a penguin with a red hat")
                .responseFormat("url")
                .size("256x256")
                .user("testing")
                .n(2)
                .build();

        List<Image> images = service.createImageEdit(createImageRequest, filePath, maskPath).getData();
        assertEquals(2, images.size());
        assertNotNull(images.get(0).getUrl());
    }

    @Test
    void createImageVariation() {
        CreateImageVariationRequest createImageVariationRequest = CreateImageVariationRequest.builder()
                .responseFormat("url")
                .size("256x256")
                .user("testing")
                .n(2)
                .build();

        List<Image> images = service.createImageVariation(createImageVariationRequest, filePath).getData();
        assertEquals(2, images.size());
        assertNotNull(images.get(0).getUrl());
    }
}
