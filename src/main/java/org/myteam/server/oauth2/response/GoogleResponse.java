package org.myteam.server.oauth2.response;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import static org.myteam.server.oauth2.constant.OAuth2ServiceProvider.GOOGLE;

public class GoogleResponse implements OAuth2Response{

    private final Map<String, Object> attribute;

    public GoogleResponse(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getProvider() {
        return GOOGLE;
    }

    @Override
    public String getProviderId() {
        return StringUtils.defaultString((String) attribute.get("sub"), "");
    }

    @Override
    public String getEmail() {
        return StringUtils.defaultString((String) attribute.get("email"), "");
    }

    public String getName() {
        return StringUtils.defaultString((String) attribute.get("name"), "");
    }
}
