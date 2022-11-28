package com.aanovik42.smartmemecreatorbot.restclient;

import com.aanovik42.smartmemecreatorbot.entity.MemeTemplate;
import com.aanovik42.smartmemecreatorbot.restclient.exception.ErrorResponse;
import com.aanovik42.smartmemecreatorbot.restclient.exception.ErrorResponseConverter;
import com.aanovik42.smartmemecreatorbot.restclient.exception.TextLineTooLongException;
import com.aanovik42.smartmemecreatorbot.restclient.model.RequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.client.Traverson;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.*;

@Service
public class RestClient {

    private final RestTemplate restTemplate;
    private final Traverson traverson;
    private final ErrorResponseConverter errorResponseConverter;

    @Autowired
    public RestClient(RestTemplate restTemplate, Traverson traverson, ErrorResponseConverter errorResponseConverter) {

        this.restTemplate = restTemplate;
        this.traverson = traverson;
        this.errorResponseConverter = errorResponseConverter;
    }

    public List<MemeTemplate> getAllMemeTemplates() {

        ParameterizedTypeReference<CollectionModel<MemeTemplate>> memeTemplateType =
                new ParameterizedTypeReference<>() {
                };
        CollectionModel<MemeTemplate> memeTemplatesCollectionModel = traverson
                .follow("templates")
                .toObject(memeTemplateType);
        Collection<MemeTemplate> memeTemplatesCollection = memeTemplatesCollectionModel.getContent();
        List<MemeTemplate> memeTemplates = new ArrayList<>(memeTemplatesCollection);

        return memeTemplates;
    }

    public MemeTemplate getMemeTemplate(Long memeTemplateId) {

        Map<String, Object> templateParams = new HashMap<>();
        templateParams.put("id", memeTemplateId);

        ParameterizedTypeReference<EntityModel<MemeTemplate>> memeTemplateType =
                new ParameterizedTypeReference<EntityModel<MemeTemplate>>() {
                };
        EntityModel<MemeTemplate> memeTemplateEntityModel = traverson
                .follow("templates")
                .withTemplateParameters(templateParams)
                .toObject(memeTemplateType);
        MemeTemplate memeTemplate = memeTemplateEntityModel.getContent();

        return memeTemplate;
    }

    public String createMemeAndGetImageUri(RequestModel requestModel) throws RuntimeException {

        try {
            String requestUri = traverson.
                    follow("memes")
                    .asLink()
                    .getHref();

            URI imageUri = restTemplate.postForLocation(requestUri, requestModel);
            return imageUri.toString();
        } catch (HttpClientErrorException.UnprocessableEntity e) {
            ErrorResponse errorResponse = errorResponseConverter.convertErrorResponseJsonToPojo(e.getResponseBodyAsString());
            String errorMessage = errorResponse.getMessage();
            if(errorResponse.getMessage().startsWith("Line is too long:")) {
                String formattedErrorMessage = errorMessage.replaceFirst("Line is too long:",
                        "<b>Line is too long:</b>");
                throw new TextLineTooLongException(errorMessage, formattedErrorMessage);
            }
            throw e;
        }
    }
}
