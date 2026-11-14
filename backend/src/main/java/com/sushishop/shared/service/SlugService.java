package com.sushishop.shared.service;

import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.function.Predicate;

@Service
public class SlugService {

    private static final int MAX_SLUG_LENGTH = 100;
    private static final String FALLBACK_SLUG = "item";

    public String generateSlug(String input) {
        if (input == null || input.isBlank()) {
            return "";
        }

        String slug = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("[^a-zA-Z0-9\\s]", "")
                .trim()
                .replaceAll("\\s+", "-")
                .toLowerCase();

        if (slug.length() > MAX_SLUG_LENGTH) {
            // Cutting mid-name can leave a dangling "-" at the end.
            slug = slug.substring(0, MAX_SLUG_LENGTH).replaceAll("-+$", "");
        }

        return slug;
    }

    /**
     * Generates a slug that doesn't exist yet by appending "-1", "-2", ...
     * Names without any Latin letters or digits (e.g. Cyrillic only) produce an
     * empty base slug, so {@value #FALLBACK_SLUG} is used instead of "" / "-1".
     */
    public String generateUniqueSlug(String input, Predicate<String> slugExists) {
        var baseSlug = generateSlug(input);
        if (baseSlug.isEmpty()) {
            baseSlug = FALLBACK_SLUG;
        }
        var uniqueSlug = baseSlug;
        int counter = 1;

        while (slugExists.test(uniqueSlug)) {
            uniqueSlug = baseSlug + "-" + counter;
            counter++;
        }
        return uniqueSlug;
    }
}