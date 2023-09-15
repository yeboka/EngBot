package com.yerbo.engbot.services;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.v3.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoogleTranslateService {

    private final String projectId = "engbot-398416";
    private String targetLanguage;

    public String[] translateText(String text)
            throws IOException {

        StringBuilder translate = new StringBuilder();
        String[] res = new String[2];

        detectLang(text);

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
        if (targetLanguage.equals("ru")) {
            res[0] = text;
            res[1] = translate.toString();
        } else {
            res[0] = translate.toString();
            res[1] = text;
        }

        return res;
    }

    public void detectLang(String text) {
        Translate translate = TranslateOptions.getDefaultInstance().getService();

        List<String> words = new ArrayList<>();
        words.add(text);

        String lang = translate.detect(words).get(0).getLanguage();
        if (lang.equals("ru")) {
            targetLanguage = "en";
        }
        else if (lang.equals("en")){
            targetLanguage = "ru";
        }
    }

}
