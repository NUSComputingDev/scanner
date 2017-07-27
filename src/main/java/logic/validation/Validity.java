package logic.validation;

import database.io.Collected;
import database.io.Survey;
import objects.Scan;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Validity {
    private Collected collected = null;
    private Survey survey = null;

    public void setSurvey(Survey survey) { this.survey = survey;}

    public void setCollected(Collected collected) { this.collected = collected;}

    public boolean hasCollected(Scan card) {
        if (collected.hasCollected(card.getMatricNumber())) {
            return true;
        }
        return false;
    }

    public boolean doneSurvey(Scan card) {
        if (survey.doneSurvey(card.getMatricNumber())) {
            return true;
        }
        return false;
    }

    public String getName(Scan card) {
        return survey.getName(card.getMatricNumber());
    }

    public String facultyOfPerson(Scan card) throws NoSuchAlgorithmException {
        String accessCode = card.getAccessCode();

        switch (accessCode) {
            case "6d05621ab7cb7b4fb796ca2ffbe1a141e0d4319d3deb6a05322b9de85d69b923": return "SoC";
            case "6f4b6612125fb3a0daecd2799dfd6c9c299424fd920f9b308110a2c1fbd8f443": return "CEG";
        }

        return "Non-SoC";
    }

    public void recordCollection(Scan card) {
        collected.recordCollection(card);
    }

    public static String getTimeStamp() {
        String dateTimeStamp = new SimpleDateFormat("dd.MM.YY HH:mm:ss").format(new Date());
        return dateTimeStamp;
    }

    public static String getHash(String msg) throws NoSuchAlgorithmException {
        MessageDigest md = null;
        md = MessageDigest.getInstance("SHA-256");
        md.update(msg.getBytes());
        byte byteData[] = md.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }
}
