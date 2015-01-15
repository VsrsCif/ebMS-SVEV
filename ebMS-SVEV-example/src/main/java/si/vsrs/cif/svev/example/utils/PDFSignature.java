/*
 * Copyright 2015, Supreme Court Republic of Slovenia 
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they will be approved by 
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the Licence is distributed on an "AS IS" basis, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and  
 * limitations under the Licence.
 */
package si.vsrs.cif.svev.example.utils;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfDate;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfPKCS7;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfSignature;
import com.lowagie.text.pdf.PdfSignatureAppearance;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfString;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.HashMap;


public class PDFSignature {


    public PDFSignature() {

    }

    public File signPDF(File document, InputStream keystore, String password, String keyPassord, String keystoreType, String alias, boolean bshowVisualization) {
        if (document == null || !document.exists()) {
            throw new RuntimeException("Error reading pdf");
        }

        String name = document.getName();
        String substring = name.substring(0, name.lastIndexOf("."));

        File outputDocument = new File(document.getParent(), substring + "_signed.pdf");

        try (FileInputStream fis = new FileInputStream(document);
                FileOutputStream fout = new FileOutputStream(outputDocument)) {

            KeyStore ks = KeyStore.getInstance(keystoreType);
            ks.load(keystore, password.toCharArray());
            PrivateKey key = (PrivateKey) ks.getKey(alias, keyPassord.toCharArray());
            Certificate[] chain = ks.getCertificateChain(alias);
            X509Certificate xcert = (X509Certificate) chain[0];
            PdfReader reader = new PdfReader(fis);

            char tmpPdfVersion = '\0'; // default version - the same as input
            final PdfStamper stp = PdfStamper.createSignature(reader, fout, tmpPdfVersion, null, true);
            final PdfSignatureAppearance sap = stp.getSignatureAppearance();
            sap.setCrypto(key, chain, null, PdfSignatureAppearance.WINCER_SIGNED);
            sap.setReason("Testni podpis");
            sap.setLocation("Maribor");
            sap.setContact(xcert.getSubjectDN().getName());

//            sap.setLayer2Text("");
            //          sap.setLayer4Text("");
            sap.setAcro6Layers(true); // --:> 

            Rectangle rc = reader.getPageSize(1);
            if (bshowVisualization) {
                sap.setVisibleSignature(
                        new Rectangle(5, rc.getHeight() - 40,
                                240, rc.getHeight() - 5), 1, null);
            }

            final PdfSignature dic = new PdfSignature(PdfName.ADOBE_PPKLITE, new PdfName("adbe.pkcs7.detached"));

            dic.setReason(sap.getReason());
            dic.setLocation(sap.getLocation());
            dic.setContact(sap.getContact());
            dic.setDate(new PdfDate(sap.getSignDate()));
            sap.setCryptoDictionary(dic);
            final int contentEstimated =  15000 ;
            final HashMap<PdfName, Integer> exc = new HashMap<>();
            exc.put(PdfName.CONTENTS, contentEstimated * 2 + 2);
            sap.preClose(exc);

            PdfPKCS7 sgn = new PdfPKCS7(key, chain, null, "SHA-256", null, false);
            InputStream data = sap.getRangeStream();
            final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte buf[] = new byte[8192];
            int n;
            while ((n = data.read(buf)) > 0) {
                messageDigest.update(buf, 0, n);
            }
            byte hash[] = messageDigest.digest();
            Calendar cal = Calendar.getInstance();

            byte sh[] = sgn.getAuthenticatedAttributeBytes(hash, cal, null);
            sgn.update(sh, 0, sh.length);

            byte[] encodedSig = sgn.getEncodedPKCS7(hash, cal, null, null);

            byte[] paddedSig = new byte[contentEstimated];
            System.arraycopy(encodedSig, 0, paddedSig, 0, encodedSig.length);

            PdfDictionary dic2 = new PdfDictionary();
            dic2.put(PdfName.CONTENTS, new PdfString(paddedSig).setHexWriting(true));

            sap.close(dic2);
        } catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException | DocumentException | InvalidKeyException | NoSuchProviderException | SignatureException ex) {
             throw new RuntimeException(ex.getMessage(), ex);
        }

        return outputDocument;
    }


}