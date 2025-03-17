package com.coffee.url_shortener.service.url;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import io.nayuki.qrcodegen.QrCode;

public class AliasGenerator {
    public static String generateAlias(String fullUrl) {
        List<String> t = Arrays.asList(fullUrl.concat(UUID.randomUUID().toString()).split(""));
        Collections.shuffle(t);

        String s = "";
        s = String.join(s, t);

        return Base64.getEncoder().withoutPadding().encodeToString(s.getBytes()).substring(0, 6);
    }

    public static BufferedImage generateQrcode(String barcodeText) throws IllegalArgumentException {
        QrCode qrCode = QrCode.encodeText(barcodeText, QrCode.Ecc.MEDIUM);

        return toImage(qrCode);
    }

    private static BufferedImage toImage(QrCode qr) {
        Objects.requireNonNull(qr);
        if (qr.size + 10 * 2L > Integer.MAX_VALUE / 4)
            throw new IllegalArgumentException("Scale or border too large");

        BufferedImage result = new BufferedImage((qr.size + 10 * 2) * 4, (qr.size + 10 * 2) * 4,
                BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < result.getHeight(); y++) {
            for (int x = 0; x < result.getWidth(); x++) {
                boolean color = qr.getModule(x / 4 - 10, y / 4 - 10);
                result.setRGB(x, y, color ? 0 : 16777215);
            }
        }
        return result;
    }
}
