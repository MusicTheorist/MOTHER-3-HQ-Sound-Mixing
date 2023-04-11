package org.musictheorist.mother3hqaudiopatcher.resources;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.musictheorist.mother3hqaudiopatcher.patching.PatchIO;
import org.musictheorist.mother3hqaudiopatcher.patching.PatchStates;
import org.musictheorist.mother3hqaudiopatcher.patching.reads.UnusedBytes;
import org.musictheorist.mother3hqaudiopatcher.resources.fetchers.ImagesFetcher;
import org.musictheorist.mother3hqaudiopatcher.rom.sound.SampleRates;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public final class PatcherResources {
    private final HostServices hostServices;

    private ImageView[] alertIcons;
    private Image globeIcon;
    private ArrayList<Image> uiIcons;

    private ObservableList<String> monospaceFonts;
    private int knownFontIndex;

    private File lastOpenedROM;

    private boolean wasNewROMLoaded;
    private ROMInfo loadedROMInfo;

    private boolean backupROM;
    private String backupPath;

    private PatchIO patchIO;

    public PatcherResources(Application application) {
        this.hostServices = application.getHostServices();
        this.lastOpenedROM = null;
        this.loadedROMInfo = null;
    }

    public HostServices getHostServices() {
        return hostServices;
    }

    public void loadImages() throws IOException {
        ImagesFetcher alertsFetcher = ImagesFetcher.newAlertsFetcher();
        ImagesFetcher  globeFetcher = ImagesFetcher.newGlobeFetcher();
        ImagesFetcher  iconsFetcher = ImagesFetcher.newIconsFetcher();

        Image[] alertImages = alertsFetcher.getImages().toArray(Image[]::new);
        alertIcons = new ImageView[alertImages.length];
        for(int i = 0; i < alertIcons.length; i++) {
            alertIcons[i] = new ImageView(alertImages[i]);
            alertIcons[i].setFitWidth(0);
            alertIcons[i].setFitHeight(0);
            alertIcons[i].setSmooth(false);
        }

        globeIcon = globeFetcher.getImages().get(0);
        uiIcons = iconsFetcher.getImages();
    }

    public ImageView getAlertIcon(AlertType alertType) {
        switch(alertType) {
            case CONFIRMATION: return alertIcons[0];
            case ERROR:        return alertIcons[1];
            case INFORMATION:  return alertIcons[2];
            case WARNING:      return alertIcons[4];
            default:           return alertIcons[3];
        }
    }
    public Image getGlobeIcon() {
        return globeIcon;
    }
    public ArrayList<Image> getIcons() {
        return uiIcons;
    }

    // Courtesy of https://yo-dave.com/2015/07/27/finding-mono-spaced-fonts-in-javafx/
    private ObservableList<String> initMonospaceFontList() {
        // Compare the layout widths of two strings. One string is composed
        // of "thin" characters, the other of "wide" characters. In mono-spaced
        // fonts the widths should be the same.
        final Text  thinText = new Text("1 l"); // note the space
        final Text thickText = new Text("MWX");
        List<String> allFonts = Font.getFamilies();
        List<String> monoFamilyList = new ArrayList<String>();
        Font font;
        for (String fontFamilyName : allFonts) {
            font = Font.font(fontFamilyName, FontWeight.NORMAL, FontPosture.REGULAR, 14.0d);
            thinText.setFont(font);
            thickText.setFont(font);
            if (thinText.getLayoutBounds().getWidth() == thickText.getLayoutBounds().getWidth()) {
                monoFamilyList.add(fontFamilyName);
            }
        }

        // Kudos to JumpmanFR for this suggestion!
        knownFontIndex = -1;
        for(int i = 0; i < monoFamilyList.size(); i++) {
            String fontName = monoFamilyList.get(i);
            if(fontName.equals("Courier New") || fontName.equals("Monospaced")) {
                knownFontIndex = i;
                break;
            }
        }

        return FXCollections.observableArrayList(monoFamilyList);
    }
    public void initMonospaceFonts() {
        this.monospaceFonts = initMonospaceFontList();
    }
    public ObservableList<String> getFonts() {
        return monospaceFonts;
    }
    public int getKnownFontIndex() {
        return knownFontIndex;
    }

    public void setLastOpenedROM(File lastOpenedROM) {
        this.lastOpenedROM = lastOpenedROM;
    }
    public File getLastOpenedROM() {
        return lastOpenedROM;
    }
    public File getLastOpenedFolder() {
        return lastOpenedROM != null ? lastOpenedROM.getParentFile() : null;
    }

    public void toggleNewROMLoaded(boolean wasNewROMLoaded) {
        this.wasNewROMLoaded = wasNewROMLoaded;
    }
    public boolean wasNewROMLoaded() {
        return wasNewROMLoaded;
    }

    public void setLoadedROMInfo(ROMInfo loadedROMInfo) {
        this.loadedROMInfo = loadedROMInfo;
    }
    public ROMInfo getLoadedROMInfo() {
        return loadedROMInfo;
    }

    public void saveNewROMPath(Path loadedROMPath) {
        loadedROMInfo = new ROMInfo(loadedROMPath);
    }
    public Path getLoadedROMPath() {
        if(loadedROMInfo != null) return loadedROMInfo.getROMPath();
        else return null;
    }

    public void setPatchType(PatchStates patchType) {
        loadedROMInfo.setPatchType(patchType);
    }
    public PatchStates getPatchType() {
        if(loadedROMInfo != null) return loadedROMInfo.getPatchType();
        else return null;
    }

    public void setSampleRate(SampleRates sampleRate) {
        loadedROMInfo.setSampleRate(sampleRate);
    }
    public SampleRates getSampleRate() {
        SampleRates sampleRate = loadedROMInfo.getSampleRate();
        // Failsafe in case all sample rates are disabled in patcher_xx.properties
        if(sampleRate == null) return SampleRates.DEFAULT;
        else return sampleRate;
    }

    public void setPatchedMixerAddress(int patchedMixerAddress) {
        loadedROMInfo.setPatchedMixerAddress(patchedMixerAddress);
    }
    public int getPatchedMixerAddress() {
        return loadedROMInfo.getPatchedMixerAddress();
    }

    public void setOverwrittenMixerData(UnusedBytes overwrittenMixerData) {
        loadedROMInfo.setOverwrittenMixerData(overwrittenMixerData);
    }
    public UnusedBytes getOverwrittenMixerData() {
        return loadedROMInfo.getOverwrittenMixerData();
    }

    public void toggleAltJapanPatch(boolean altJapanPatch) {
        loadedROMInfo.toggleAltJapanPatch(altJapanPatch);
    }
    public boolean isAltJapanPatchEnabled() {
        return loadedROMInfo.isAltJapanPatchEnabled();
    }

    public void toggleROMBackup(boolean backupROM) {
        this.backupROM = backupROM;
    }
    public boolean isROMBackupEnabled() {
        return backupROM;
    }

    public void clearBackupPath() {
        backupPath = "";
    }
    public void setBackupPath(String backupPath) {
        this.backupPath = backupPath;
    }
    public String getBackupPath() {
        return backupPath;
    }

    public void setPatchIO(PatchIO patchIO) {
        this.patchIO = patchIO;
    }
    public PatchIO getPatchIO() {
        return patchIO;
    }
}