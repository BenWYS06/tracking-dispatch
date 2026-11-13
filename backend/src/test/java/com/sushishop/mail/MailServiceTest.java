package com.sushishop.mail;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MailServiceTest {

    @Test
    void shouldMaskLocalPartOfEmail() {
        assertThat(MailService.maskEmail("john.doe@example.com")).isEqualTo("j***@example.com");
    }

    @Test
    void shouldMaskSingleCharLocalPart() {
        assertThat(MailService.maskEmail("a@example.com")).isEqualTo("a***@example.com");
    }

    @Test
    void shouldFullyMaskMalformedEmail() {
        assertThat(MailService.maskEmail("not-an-email")).isEqualTo("***");
        assertThat(MailService.maskEmail("@example.com")).isEqualTo("***");
    }

    @Test
    void shouldReturnNullForNullEmail() {
        assertThat(MailService.maskEmail(null)).isNull();
    }
}
