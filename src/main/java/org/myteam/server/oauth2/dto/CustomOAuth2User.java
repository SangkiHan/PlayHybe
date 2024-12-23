package org.myteam.server.oauth2.dto;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Getter
public class CustomOAuth2User implements OAuth2User {
    private final String NOT_USED = "Not Used";
    private String username;
    private String role;
    private UUID publicId;
    public CustomOAuth2User(String username, String role, UUID publicId) {
        this.username = username;
        this.role = role;
        this.publicId = publicId;
    }
    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }
    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return role;
            }
        });
        return collection;
    }

    // 미사용
    @Override
    public String getName() {
        return NOT_USED;
    }

    public UUID getPublicId() { return publicId; }
}
