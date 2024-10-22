package com.codemagic.catalog.admin.application.media.get;

public record GetMediaCommand(String id, String type) {
    public static GetMediaCommand with(final String id, final String type) {
        return new GetMediaCommand(id, type);
    }
}
