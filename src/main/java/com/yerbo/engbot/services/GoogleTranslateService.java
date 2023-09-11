package com.yerbo.engbot.services;

import com.google.cloud.translate.v3.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class GoogleTranslateService {

    private final String projectId = "engbot-398416";
    private String targetLanguage = "ru";


    public String translateText(String text)
            throws IOException {

        StringBuilder translate = new StringBuilder();

        try (TranslationServiceClient client = TranslationServiceClient.create()) {
            LocationName parent = LocationName.of(projectId, "global");

            TranslateTextRequest request =
                    TranslateTextRequest.newBuilder()
                            .setParent(parent.toString())
                            .setMimeType("text/plain")
                            .setTargetLanguageCode(targetLanguage)
                            .addContents(text)
                            .build();

            TranslateTextResponse response = client.translateText(request);

            for (Translation translation : response.getTranslationsList()) {
                translate.append(translation.getTranslatedText());
            }
        }

        return translate.toString();
    }

}
